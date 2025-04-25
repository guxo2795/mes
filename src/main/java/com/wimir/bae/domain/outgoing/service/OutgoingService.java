package com.wimir.bae.domain.outgoing.service;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractShipmentItemInfoDTO;
import com.wimir.bae.domain.contract.mapper.ContractItemMapper;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.mapper.InventoryProductMapper;
import com.wimir.bae.domain.outgoing.dto.*;
import com.wimir.bae.domain.outgoing.mapper.OutgoingMapper;
import com.wimir.bae.domain.outsource.mapper.OutsourceMapper;
import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanTotalSearchDTO;
import com.wimir.bae.domain.plan.mapper.PlanMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.mapper.WarehouseMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OutgoingService {

    private final ContractItemMapper contractItemMapper;
    private final ContractMapper contractMapper;
    private final PlanMapper planMapper;
    private final OutgoingMapper outgoingMapper;
    private final OutsourceMapper outsourceMapper;
    private final WarehouseMapper warehouseMapper;
    private final InventoryProductMapper inventoryProductMapper;

    public void createOutgoing(UserLoginDTO userLoginDTO, OutgoingShipmentRegDTO outgoingShipmentRegDTO) {

        String contractKey = outgoingShipmentRegDTO.getContractKey();
        ContractInfoDTO contractInfo = contractMapper.getContractInfo(contractKey);
        // 수주 품목 정보
        List<ContractShipmentItemInfoDTO> contractShipmentItemList = Optional.ofNullable(contractItemMapper.getContractShipmentItemInfo(contractKey))
                .orElse(Collections.emptyList());
        // 수주-생산 계획 정보 조회
        PlanInfoDTO planInfoDTO = planMapper.getProductionPlan(contractInfo.getContractCode());
        if (planInfoDTO == null) {
            throw new CustomRuntimeException("잘못된 수주 정보이거나 해당 수주 정보가 없습니다. 다시 확인해 주세요.");
        }

        String quantity = "";
        // 수주 품목 확정 및 개수 확인
        for (ContractShipmentItemInfoDTO contractShipmentItem : contractShipmentItemList) {

            // 생산 실적 테이블 조회
            PlanTotalSearchDTO planTotalSearchDTO = planMapper.getProductPlanTotal(planInfoDTO.getPlanKey(), contractShipmentItem.getProductKey());

            // 생산 계획 미완료 확인
            if (planTotalSearchDTO == null) {
                throw new CustomRuntimeException("아직 생산계획이 완료되지 않았습니다. 생산계획을 모두 완료 후 출하를 진행해 주십시오.");
            }

            // (완제품 (F), 자재 (M))
            switch (contractShipmentItem.getAssetTypeFlag()) {
                // 완제품
                case "F":
                    // 생산 계획이 완료 되었는지 판단
                    if ("0".equals(planTotalSearchDTO.getIsCompleted())) {
                        throw new CustomRuntimeException("완제품 작업 지시 확정 후 출하를 진행해 주세요.");
                    }

                    // 완제품의 생산 개수가 계획 개수와 일치 하는지
                    if (planTotalSearchDTO.getExecuteQuantity() < planTotalSearchDTO.getPlanQuantity()) {
                        throw new CustomRuntimeException("완제품 생산 수량이 수주 계획 총 수량보다 생산 수량이 적으므로 출하를 진행할 수 없습니다.");
                    }

                    // 이미 해당 수주의 완제품이 등록되어 있다면 등록 불가
                    if (outgoingMapper.isShipmentDuplicateCheck(contractShipmentItem.getProductKey(), contractShipmentItem.getContractMaterialKey())) {
                        throw new CustomRuntimeException("이미 해당 수주의 같은 품목이 출하 등록 되어있습니다.");
                    }

                    // 완제품의 제품 정보 저장
                    outgoingShipmentRegDTO.setProductKey(contractShipmentItem.getProductKey());
                    outgoingShipmentRegDTO.setFinishQuantity(String.valueOf(planTotalSearchDTO.getPlanQuantity()));
                    outgoingShipmentRegDTO.setContractMaterialKey(contractShipmentItem.getContractMaterialKey());
                    quantity = contractShipmentItem.getContractQuantity();

                    // 출하 등록
                    outgoingMapper.createShipment(outgoingShipmentRegDTO);
                    break;
                case "M":
                    // BOM에 연결된 자재들이 실행중인지 판단
                    if ("0".equals(planTotalSearchDTO.getIsCompleted())) {
                        throw new CustomRuntimeException("자재 작업 지시 확정 후 출하를 진행해 주세요.");
                    }
                    // 자재의 생산 개수가 계획 개수와 일치 하는지
                    if (planTotalSearchDTO.getExecuteQuantity() < planTotalSearchDTO.getPlanQuantity()) {
                        throw new CustomRuntimeException("자재 생산 수량이 수주 계획 수량보다 적으므로 출하를 진행할 수 없습니다.");
                    }
                    break;
                default:
                    throw new CustomRuntimeException("잘못된 유형의 제품이 존재 합니다. 다시 확인해 주세요.");
            }

        }

    }

    @Transactional(readOnly = true)
    public List<OutgoingShipmentInfoDTO> getShipmentList() {

        // 출고/출하 목록
        List<OutgoingShipmentInfoDTO> outgoingShipmentInfoDTOList = outgoingMapper.getOutgoingShipmentList();
        
        List<OutgoingShipmentInfoDTO> outgoingShipmentInfoList = new ArrayList<>();
        for(OutgoingShipmentInfoDTO outgoingShipmentInfoDTO : outgoingShipmentInfoDTOList) {
            String outsourceStatus = outsourceMapper.getOutsourceStatus(outgoingShipmentInfoDTO.getContractCode(), "14");
            outgoingShipmentInfoDTO.setOutsourceStatus(outsourceStatus);
            outgoingShipmentInfoList.add(outgoingShipmentInfoDTO);
        }
        return outgoingShipmentInfoList;
    }

    public void setOutgoingUpdate(UserLoginDTO userLoginDTO, OutgoingShipmentUpdateDTO outgoingShipmentUpdateDTO) {

        if(!warehouseMapper.isWarehouseExist(outgoingShipmentUpdateDTO.getWarehouseKey())){
            throw new CustomRuntimeException("존재하지 않는 창고입니다. 다시 확인해 주세요.");
        }

        outgoingMapper.updateOutgoing(outgoingShipmentUpdateDTO);
    }

    public void deleteOutgoing(UserLoginDTO userLoginDTO, List<String> outgoingKeyList) {

        for(String outgoingKey : outgoingKeyList) {
            outgoingInfoDTO outgoingInfo = outgoingMapper.getOutgoingInfo(outgoingKey);
            outgoingMapper.deleteOutgoing(outgoingKey);
        }
    }

    public void outgoingComplete(UserLoginDTO userLoginDTO, List<OutgoingCompleteRegDTO> outgoingCompleteInfo) {

        // 수주 종결 처리 리스트
        for(OutgoingCompleteRegDTO completeInfo : outgoingCompleteInfo) {
            // 수주 정보 조회(수주 품목 키 이용)
            ContractInfoDTO contractInfo = contractItemMapper.getContractCompleteInfo(completeInfo.getMaterialKey());
            
            if(!contractInfo.getIsCompleted().equals("2")){
                throw new CustomRuntimeException("실행 중인 수주만 종결할 수  있습니다.");
            }
            // 수주 완료
            contractMapper.completeContract(contractInfo.getContractCode());
            
            // 생산 계획 정보 조회
            PlanInfoDTO planInfoDTO = planMapper.getProductionPlan(contractInfo.getContractCode());
            // 생산 실적 결과 조회
            PlanTotalSearchDTO planTotalSearchDTO = planMapper.getProductPlanTotal(planInfoDTO.getPlanKey(), completeInfo.getProductKey());
            
            // 재고 확인
            double currentQuantity = inventoryProductMapper.getProductInventory(completeInfo.getProductKey(), completeInfo.getWarehouseKey());
            if(currentQuantity < completeInfo.getOutgoingQuantity()) {
                throw new CustomRuntimeException("현재 출하하려는 제품의 재고가 부족합니다.");
            }
            // 외주 종결 확인
            if(!outgoingMapper.isOutsourceComplete(contractInfo.getContractCode(), "14")) {
                throw new CustomRuntimeException("미완료된 외주 항목이 있습니다. 다시 확인해 주십시오.");
            }

//            // 외주 자재 재고 감소(사내 생산 제품이 없을 경우 감소 진행_실적 확성 시 외주 재고 감소)_(외주/도급만 check)
//            if (!outsourceMapper.isInternalProduct(contractInfo.getContractCode())) {
//
//                // 현 BOM의 모든 외주품 리스트
//                List<OutsourceItemDTO> outsourceItemDTOList = outsourceMapper.getAllOutsourceItemList(contractInfo.getContractCode(), processOutsourcedKey);
//                for (OutsourceItemDTO itemDTO : outsourceItemDTOList) {
//
//                    // 품목 외주수량 및 현재 inventory 수량 조회
//                    OutsourceInventoryCheckDTO checkDTO = outsourceMapper.getOutsourceInventoryCheck(itemDTO.getProductKey(), contractInfo.getContractCode());
//                    if (checkDTO.getResult() < 0) {
//                        throw new CustomRuntimeException(checkDTO.getResult() == -2.0 ? "정상 처리된 외주 품목이 아닙니다. 다시 확인해 주세요."
//                                : "재고가 부족합니다.");
//                    }
//
//                    // outgoing 등록
//                    OutgoingRegDBDTO outgoingRegDBDTO = OutgoingRegDBDTO.builder()
//                            .productKey(itemDTO.getProductKey())
//                            .warehouseKey(checkDTO.getWarehouseKey())
//                            .materialKey(contractInfo.getContractCode())
//                            .outgoingTypeFlag("C1")
//                            .outgoingDateTime(TimeUtil.getNowDateTime())
//                            .quantity(Double.parseDouble(itemDTO.getQuantity()))
//                            .outboundUserKey(userLoginDTO.getUserCode())
//                            .build();
//                    outgoingMapper.createOutgoingToDB(outgoingRegDBDTO);
//
//                    inventoryProductMapper.decreaseInventory(itemDTO.getProductKey(), checkDTO.getWarehouseKey(), itemDTO.getQuantity());
//                }
//            }

            // 출고 완료
            String correctionDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            outgoingMapper.outgoingComplete(completeInfo.getOutgoingKey(), correctionDateTime, userLoginDTO.getUserCode());
            
            // 재고 감소
            double correctionQuantity = currentQuantity - planTotalSearchDTO.getPlanQuantity();
            InventoryCorrectionDTO inventoryCorrectionDTO = InventoryCorrectionDTO.builder()
                    .productKey(completeInfo.getProductKey())
                    .warehouseKey(completeInfo.getWarehouseKey())
                    .quantity(String.valueOf(correctionQuantity))
                    .build();
            inventoryProductMapper.setProductInventoryCorrection(inventoryCorrectionDTO);
        }
    }

    @Transactional(readOnly = true)
    public List<OutgoingShipmentEndInfoDTO> getShipmentEndList() {
        return Optional.ofNullable(outgoingMapper.getOutgoingShipmentEndList())
                .orElse(Collections.emptyList());
    }
}
