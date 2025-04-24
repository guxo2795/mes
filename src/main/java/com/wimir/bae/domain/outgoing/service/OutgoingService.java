package com.wimir.bae.domain.outgoing.service;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractShipmentItemInfoDTO;
import com.wimir.bae.domain.contract.mapper.ContractItemMapper;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.outgoing.dto.OutgoingShipmentInfoDTO;
import com.wimir.bae.domain.outgoing.dto.OutgoingShipmentRegDTO;
import com.wimir.bae.domain.outgoing.dto.OutgoingShipmentUpdateDTO;
import com.wimir.bae.domain.outgoing.dto.outgoingInfoDTO;
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
}
