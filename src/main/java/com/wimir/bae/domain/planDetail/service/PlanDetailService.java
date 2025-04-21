package com.wimir.bae.domain.planDetail.service;

import com.wimir.bae.domain.plan.mapper.PlanMapper;
import com.wimir.bae.domain.planDetail.dto.*;
import com.wimir.bae.domain.planDetail.mapper.PlanDetailMapper;
import com.wimir.bae.domain.planTotal.mapper.PlanTotalMapper;
import com.wimir.bae.domain.quality.mapper.FaultStateMapper;
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
public class PlanDetailService {

    private final PlanDetailMapper planDetailMapper;
    private final FaultStateMapper faultStateMapper;
    private final PlanTotalMapper planTotalMapper;

    public void createPlanDetailQuantity(PlanDetailRegDTO regDTO, UserLoginDTO userLoginDTO) {

        if(planDetailMapper.isExistDetail(regDTO.getPlanKey(), regDTO.getProductKey(), regDTO.getExecuteDate())){
            throw new CustomRuntimeException("이미 등록 되어있는 실적입니다.");
        }

        // 수주 수량을 넘어서면 안됨.
        int inputQuantity = Integer.parseInt(regDTO.getExecuteQuantity());
        int totalRegisteredQuantity = Optional.ofNullable(planDetailMapper.getTotalRegisteredQuantity(regDTO.getPlanKey(), regDTO.getProductKey())).orElse(0);
        int orderedQuantity = Optional.ofNullable(planDetailMapper.getPlanOrderedQuantity(regDTO.getProductKey())).orElse(0);
        if(totalRegisteredQuantity + inputQuantity > orderedQuantity){
            throw new CustomRuntimeException("등록된 수량이 수주 수량을 초과할 수 없습니다.");
        }

        planDetailMapper.createPlanDetailQuantity(regDTO);
    }

    @Transactional(readOnly = true)
    public List<DetailInfoDTO> getDetailList(DetailSearchDTO searchDTO) {
        return Optional.ofNullable(planDetailMapper.getDetailList(searchDTO))
                .orElse(Collections.emptyList());
    }

    public void updatePlanDetailQuantity(PlanDetailQuantityModDTO regDTO, UserLoginDTO userLoginDTO) {

        if (!planDetailMapper.checkPlanDetailCompleted(regDTO.getDetailKey()))
            throw new CustomRuntimeException("이미 확정된 지시서입니다.");

        planDetailMapper.updatePlanDetailQuantity(regDTO);
    }

    public void deletePlanDetail(List<String> detailKeyList, UserLoginDTO userLoginDTO) {

        for (String detailKey : detailKeyList) {
            if (!planDetailMapper.checkPlanDetailCompleted(detailKey))
                throw new CustomRuntimeException("이미 확정된 지시서입니다.");
            planDetailMapper.deletePlanDetail(detailKey);
        }
    }

    public void updatePlanDetailCompleted(List<String> detailKeyList, UserLoginDTO userLoginDTO) {

        for(String detailKey : detailKeyList){
            if(!planDetailMapper.checkPlanDetailCompleted(detailKey))
                throw new CustomRuntimeException("이미 확정된 지시서입니다.");

            planDetailMapper.updatePlanDetailCompleted(detailKey);

            // 생산 품목 정보 업데이트(생산 계획 plan, 생산 실적 결과 total)
            List<DetailWarehouseDTO> list = planDetailMapper.listDetailWarehouse(detailKey);
            for (DetailWarehouseDTO dto : list) {
                if(Integer.parseInt(dto.getFaultQuantity()) > 0) {
                    // 불량 관리
                    faultStateMapper.createFaultState("P",dto.getContractName(),dto.getDetailKey(),dto.getExecuteDate(),dto.getFaultQuantity());
                    planDetailMapper.updateFaultForTotal(dto.getPlanKey(),dto.getProductKey(),Integer.parseInt(dto.getFaultQuantity()));
                }

                // 생산한 품목들을 total에 업로드
                planDetailMapper.updateExecuteForTotal(dto.getPlanKey(),dto.getProductKey(),Integer.parseInt(dto.getExecuteQuantity()));

                // 완제품일경우
                if(!planTotalMapper.checkAssetTypeFlag(dto.getResultKey())){
                    // 생산한 품목 plan에도 업로드
                    planDetailMapper.updateExecuteForPlan(dto.getPlanKey(),dto.getProductKey(),Integer.parseInt(dto.getExecuteQuantity()));
                }
            }
        }
    }
}
