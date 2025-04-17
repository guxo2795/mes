package com.wimir.bae.domain.contract.service;

import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.contract.dto.*;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.plan.mapper.PlanMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {

    private final ContractMapper contractMapper;
    private final CompanyMapper companyMapper;
    private final PlanMapper planMapper;

    public void createContract(UserLoginDTO userLoginDTO, ContractRegDTO contractRegDTO) {

        String companyTypeFlag = companyMapper.getCompanyTypeFlag(contractRegDTO.getCompanyKey());
        if(!"S".equals(companyTypeFlag)){
            throw new CustomRuntimeException("납품 업체만 등록이 가능합니다.");
        }

        contractMapper.createContract(contractRegDTO);
        contractMapper.createContractMaterials(contractRegDTO);
    }

    @Transactional(readOnly = true)
    public List<ContractInfoDTO> getContractList() {
        return Optional.ofNullable(contractMapper.getContractList())
                .orElse(Collections.emptyList());
    }

    public void updateContract(UserLoginDTO userLoginDTO, ContractModDTO modDTO) {

        ContractInfoDTO infoDTO = contractMapper.getContractInfo(modDTO.getContractCode());
        if (!"0".equals(infoDTO.getIsCompleted())) {
            throw new CustomRuntimeException("이미 실행거나 종결된 수주는 수정 할 수 없습니다.");
        }

        String companyTypeFlag = companyMapper.getCompanyTypeFlag(modDTO.getCompanyKey());
        if (!"S".equals(companyTypeFlag)) {
            throw new CustomRuntimeException("납품 업체만 등록할 수 있습니다.");
        }

        contractMapper.updateContract(modDTO);
        contractMapper.updateContractMaterial(modDTO.getContractCode(),modDTO.getProductKey(),modDTO.getQuantity());
    }

    public void deletedContract(UserLoginDTO userLoginDTO, List<String> contractCodeList) {

        for (String contractCode : contractCodeList){

            ContractInfoDTO infoDTO = contractMapper.getContractInfo(contractCode);
            if (!"0".equals(infoDTO.getIsCompleted())) {
                throw new CustomRuntimeException("종결된 수주는 삭제 할 수 없습니다.");
            }

            contractMapper.deletedContract(contractCode);
            contractMapper.deletedContractAllMaterials(contractCode);
        }
    }

    // 수주 전 자재를 가져오는 메서드
    @Transactional(readOnly = true)
    public List<ContractMaterialInfoDTO> listContractMaterial(ContractMaterialSearchDTO searchDTO) {
        return Optional.ofNullable(contractMapper.listContractMaterial(searchDTO.getContractCode(), "13"))
                .orElse(Collections.emptyList());
    }

    public void startContract(UserLoginDTO userLoginDTO, String contractCode, String planDate, List<ContractWarehouseDTO> warehouseKeyMap) {

        ContractInfoDTO infoDTO = contractMapper.getContractForPlan(contractCode);

        if("1".equals(infoDTO.getIsCompleted())){
            throw new CustomRuntimeException("이미 종결된 수주입니다. " + contractCode);
        } else if ("2".equals(infoDTO.getIsCompleted())){
            throw new CustomRuntimeException("이미 실행중인 수주입니다. " + contractCode);
        }

        try {
            // is_completed = 2, 실행 상태
            contractMapper.startContract(contractCode);

            // 사내 생산 자재
            // 수주 실행 시 품목 자재들 등록에 필요한 내용 불러오기
            List<ContractMaterialInfoDTO> materialList = contractMapper.listContractMaterial(contractCode, "13");

            // 자재에 대한 재고 차감 및 기록 업데이트
            for(ContractMaterialInfoDTO material : materialList) {
                // ex) 완제품 10개 * 완제품1개에 필요한 자재 수
                double initialQuantity = Double.parseDouble(material.getQuantity());
                double remainingQuantity = initialQuantity;

                // 해당 material 의 productKey 에 맞는 warehouseKey 찾기
                ContractWarehouseDTO matchingWarehouse = warehouseKeyMap.stream()
                        .filter(warehouse -> warehouse.getProductKey().equals(material.getProductKey()))
                        .findFirst()
                        .orElse(null);

                // 차감 수량
                double quantityToDeduct = 0;
                if (matchingWarehouse != null && matchingWarehouse.getWarehouseKey() != null) {

                    // 가용 수량
                    String availableInventoryStr = contractMapper.getAvailableInventoryAsString(material.getProductKey(), matchingWarehouse.getWarehouseKey());
                    double availableInventory = 0.0;
                    if (availableInventoryStr != null && !availableInventoryStr.trim().isEmpty()) {
                        availableInventory = Double.parseDouble(availableInventoryStr);
                    }

                    // 창고 재고보다 더 많은 수량을 차감하지 않도록
                    quantityToDeduct = Math.min(remainingQuantity, availableInventory);
                    remainingQuantity -= quantityToDeduct;

                    // 차감할 재고가 있을 경우
                    if (quantityToDeduct > 0) {
                        // 차감된 재고 업데이트
                        String updatedInventoryStr = String.valueOf(availableInventory - quantityToDeduct);
                        contractMapper.updateInventoryAsString(material.getProductKey(), matchingWarehouse.getWarehouseKey(), updatedInventoryStr);
                    }
                }

                // 사내 생산해야하는 수량
                String productionQuantityStr = String.valueOf(remainingQuantity);
                // 사내 생산 자재 기록
                ContractMaterialDBDTO materialDBDTO = new ContractMaterialDBDTO();
                materialDBDTO.setContractCode(material.getContractCode());
                materialDBDTO.setProductKey(material.getProductKey());
                materialDBDTO.setQuantity(productionQuantityStr);
                // tbl_order_contract_materials 에 수주 자재로 등록
                contractMapper.insertMaterial(materialDBDTO);
                // 사내 생산 계획 등록
                planMapper.createPlan(material.getProductKey(), material.getContractCode(), productionQuantityStr, "0", planDate);

                if (quantityToDeduct > 0) {
                    // 출고 기록 저장
                    ContractMaterialDBDTO materialDTO = new ContractMaterialDBDTO();
                    materialDTO.setContractCode(contractCode);
                    materialDTO.setProductKey(material.getProductKey());

                    String contractMaterialKey = contractMapper.selectContractMaterialKey(materialDTO);
                    materialDTO.setContractMaterialKey(contractMaterialKey);

                    contractMapper.insertOutgoingRecord(
                            material.getProductKey(),
                            matchingWarehouse.getWarehouseKey(),
                            materialDTO.getContractMaterialKey(),
                            "C",
                            planDate,
                            String.valueOf(quantityToDeduct),
                            "수주 재고 감소"
                    );
                }
            }


            // 완제품에 대한 재고 차감 및 기록 업데이트
            double productInitialQuantity = Double.parseDouble(infoDTO.getQuantity());
            double productRemainingQuantity = productInitialQuantity;

            // 해당 완제품의 productKey에 맞는 warehouseKey 찾기
            ContractWarehouseDTO productWarehouse = warehouseKeyMap.stream()
                    .filter(warehouse -> warehouse.getProductKey().equals(infoDTO.getProductKey()))
                    .findFirst()
                    .orElse(null);

            double productQuantityToDeduct = 0;
            if (productWarehouse != null && productWarehouse.getWarehouseKey() != null) {

                String productAvailableInventoryStr = contractMapper.getAvailableInventoryAsString(infoDTO.getProductKey(), productWarehouse.getWarehouseKey());
                double productAvailableInventory = 0.0;

                if (productAvailableInventoryStr != null && !productAvailableInventoryStr.trim().isEmpty()) {
                    productAvailableInventory = Double.parseDouble(productAvailableInventoryStr);
                }

                productQuantityToDeduct = Math.min(productRemainingQuantity, productAvailableInventory);
                productRemainingQuantity -= productQuantityToDeduct;

                if (productQuantityToDeduct > 0) {
                    // 완제품 재고 업데이트
                    String productUpdatedInventoryStr = String.valueOf(productAvailableInventory - productQuantityToDeduct);
                    contractMapper.updateInventoryAsString(infoDTO.getProductKey(), productWarehouse.getWarehouseKey(), productUpdatedInventoryStr);
                }
            }

            // 수주 실행 된 계획서 생성
            planMapper.createPlan(infoDTO.getProductKey(), infoDTO.getContractCode(), String.valueOf(productRemainingQuantity), "0", planDate);

            if (productQuantityToDeduct > 0) {
                // 출고 기록 저장
                ContractMaterialDBDTO materialDTO = new ContractMaterialDBDTO();

                materialDTO.setContractCode(contractCode);
                materialDTO.setProductKey(infoDTO.getProductKey());

                String contractMaterialKey = contractMapper.selectContractMaterialKey(materialDTO);
                materialDTO.setContractMaterialKey(contractMaterialKey);

                contractMapper.insertOutgoingRecord(
                        infoDTO.getProductKey(),
                        productWarehouse.getWarehouseKey(),
                        materialDTO.getContractMaterialKey(),
                        "C",
                        planDate,
                        String.valueOf(productQuantityToDeduct),
                        "수주 재고 감소"
                );
            }

        } catch(Exception e) {
            throw new CustomRuntimeException("수주 실행 중 오류 발생");
        }

    }

    public void completeContract(UserLoginDTO userLoginDTO, List<String> contractCodeList) {

        for (String contractCode : contractCodeList) {

            ContractInfoDTO infoDTO = contractMapper.getContractInfo(contractCode);

            if (!"0".equals(infoDTO.getIsCompleted())) {
                throw new CustomRuntimeException("이미 종결되거나 실행중인 수주 입니다.");
            }

            contractMapper.completeContract(contractCode);
        }
    }

    public void deletedContractMaterials(UserLoginDTO userLoginDTO, List<String> contractMaterialKeyList, String contractCode) {

        ContractInfoDTO infoDTO =contractMapper.getContractInfo(contractCode);

        if(!"0".equals(infoDTO.getIsCompleted()))
            throw new CustomRuntimeException("이미 종결된 수주 입니다.");

        for (String contractMaterialKey : contractMaterialKeyList) {

            contractMapper.deletedContractMaterials(contractMaterialKey);
        }
    }

    public List<MaterialsInfoDTO> getMaterialInfoList() {
        return Optional.ofNullable(contractMapper.getMaterialInfoList())
                .orElse(Collections.emptyList());
    }
}
