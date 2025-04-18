package com.wimir.bae.domain.planDetail.service;

import com.wimir.bae.domain.plan.mapper.PlanMapper;
import com.wimir.bae.domain.planDetail.dto.PlanDetailRegDTO;
import com.wimir.bae.domain.planDetail.mapper.PlanDetailMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanDetailService {

    private final PlanDetailMapper planDetailMapper;

    public void createPlanDetailQuantity(PlanDetailRegDTO regDTO, UserLoginDTO userLoginDTO) {

        if(planDetailMapper.isExistDetail(regDTO.getPlanKey(), regDTO.getProductKey(), regDTO.getExecuteDate())){
            throw new CustomRuntimeException("이미 등록 되어있는 실적입니다.");
        }

        // 수주 수량을 넘어서면 안됨.
        double inputQuantity = Double.parseDouble(regDTO.getExecuteQuantity());
        double totalRegisteredQuantity = Double.parseDouble(planDetailMapper.getTotalRegisteredQuantity(regDTO.getPlanKey(), regDTO.getProductKey()));
        double orderedQuantity = Double.parseDouble(planDetailMapper.getPlanOrderedQuantity(regDTO.getPlanKey(), regDTO.getProductKey()));
        if(totalRegisteredQuantity + inputQuantity > orderedQuantity){
            throw new CustomRuntimeException("등록된 수량이 수주 수량을 초과할 수 없습니다.");
        }

        planDetailMapper.createPlanDetailQuantity(regDTO);
    }
}
