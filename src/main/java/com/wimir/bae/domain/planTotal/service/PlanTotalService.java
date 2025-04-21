package com.wimir.bae.domain.planTotal.service;

import com.wimir.bae.domain.planTotal.dto.PlanTotalInfoDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalModDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalResultDTO;
import com.wimir.bae.domain.planTotal.mapper.PlanTotalMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanTotalService {

    private final PlanTotalMapper planTotalmapper;
    private final PlanTotalMapper planTotalMapper;

    @Transactional(readOnly = true)
    public List<PlanTotalInfoDTO> getResultList() {
        return Optional.ofNullable(planTotalmapper.getResultList())
                .orElse(Collections.emptyList());
    }

    public void updateResultNote(PlanTotalModDTO modDTO) {
        planTotalmapper.updateResultNote(modDTO);
    }

    public void completedResult(UserLoginDTO userLoginDTO, String resultKey, String warehouseKey) {

        if(!planTotalmapper.isCompleted(resultKey)){
            throw new CustomRuntimeException("이미 실적이 확정되어 있습니다.");
        }
        // 실적 확정
        planTotalmapper.completedResult(resultKey);

        // 실적 확정에 필요한 리스트
        List<PlanTotalResultDTO> list = planTotalmapper.listPlanTotalResult(resultKey);
        for (PlanTotalResultDTO dto : list){
            // 생산량이 계획수량보다 적을경우
            if(Double.parseDouble(dto.getPlanQuantity()) > Double.parseDouble(dto.getExecuteQuantity())) {
                throw new CustomRuntimeException("계획된 수량보다 적습니다.");
            }
            
            // 완제품일경우
            if(!planTotalmapper.checkAssetTypeFlag(resultKey)) {

                // 완제품에 필요한 자재들이 확정이 되어야함.
                if(!planTotalmapper.isCompletedMaterial(resultKey)) {
                    throw new CustomRuntimeException("확정되지 않은 자재가 남아 있습니다.");
                }
                
                // 자재관련 외주 확인
//                List<OutsourceItemDTO> outsourceItemDTOList = outsourceMapper.getAllOutsourceItemList(dto.getContractKey(), processOutsourcedKey);
//                for (OutsourceItemDTO itemDTO : outsourceItemDTOList) {
//
//                    if (itemDTO.getOutsourceState() == null || List.of("P", "O").contains(itemDTO.getOutsourceState())) {
//                        throw new CustomRuntimeException("현재 미완료된 외주가 존재합니다. 외주 완료 후 완제품 입고를 진행해 주세요.");
//                    }
//
//                    // 품목 외주수량 및 현재 inventory 수량 조회
//                    OutsourceInventoryCheckDTO checkDTO = outsourceMapper.getOutsourceInventoryCheck(itemDTO.getProductKey(), dto.getContractKey());
//                    if (checkDTO.getResult() < 0) {
//                        throw new CustomRuntimeException(checkDTO.getResult() == -2.0 ? "정상 처리된 외주 품목이 아닙니다. 다시 확인해 주세요."
//                                : "재고가 부족합니다.");
//                    }
//
//                    // outgoing 등록
//                    OutgoingRegDBDTO outgoingRegDBDTO = OutgoingRegDBDTO.builder()
//                            .productKey(itemDTO.getProductKey())
//                            .warehouseKey(checkDTO.getWarehouseKey())
//                            .materialKey(dto.getContractKey())
//                            .outgoingTypeFlag("C1")
//                            .outgoingDateTime(TimeUtil.getNowDateTime())
//                            .quantity(Double.parseDouble(itemDTO.getQuantity()))
//                            .outboundUserKey(userLoginDTO.getUserCode())
//                            .build();
//                    outgoingMapper.createOutgoingToDB(outgoingRegDBDTO);
//
//                    inventoryProductMapper.decreaseInventory(itemDTO.getProductKey(), checkDTO.getWarehouseKey(), itemDTO.getQuantity());
//
//                }
                // tbl_incoming / 자재관리- 품목증가 및 입하/입고 테이블
                planTotalMapper.insertIncomingProduct(dto.getProductKey(), warehouseKey, dto.getContractMaterialKey(), Double.parseDouble(dto.getExecuteQuantity()),"F","완제품 완성");

                planTotalmapper.updateWarehouse(warehouseKey, resultKey);
                if(planTotalMapper.checkInventory(dto.getProductKey(), warehouseKey)){
                    planTotalMapper.updateInventory(dto.getProductKey(),warehouseKey,Double.parseDouble(dto.getExecuteQuantity()));
                }else{
                    throw new CustomRuntimeException("해당 품목이 지워졌거나 비정상적인 품목입니다.");
                }


            // 실적 결과 확정 - 자재일 경우
            }else{ 

                // 자재가 남을 때
                if(Double.parseDouble(dto.getPlanQuantity())  < Double.parseDouble(dto.getExecuteQuantity()) ) {

                    double quantity  = Double.parseDouble(dto.getExecuteQuantity()) - Double.parseDouble(dto.getPlanQuantity());

                    planTotalMapper.updateWarehouse(warehouseKey,resultKey);
                    if(planTotalMapper.checkInventory(dto.getProductKey(), warehouseKey)){
                        planTotalMapper.updateInventory(dto.getProductKey(), warehouseKey, quantity);
                    }else{
                        throw new CustomRuntimeException("해당 품목이 지워졌거나 비정상적인 품목입니다.");
                    }

                    planTotalMapper.insertIncomingProduct(dto.getProductKey(), warehouseKey, dto.getContractMaterialKey(), quantity, "P", "생산되고 남은 자재");

                }

            }

        }
    }
}
