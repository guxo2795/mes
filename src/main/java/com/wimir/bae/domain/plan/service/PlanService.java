package com.wimir.bae.domain.plan.service;

import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanModDTO;
import com.wimir.bae.domain.plan.mapper.PlanMapper;
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
public class PlanService {

    private final PlanMapper planMapper;

    public List<PlanInfoDTO> getPlanList() {
        return Optional.ofNullable(planMapper.getPlanList())
                .orElse(Collections.emptyList());
    }

    public void updatePlan(UserLoginDTO userLoginDTO ,PlanModDTO modDTO) {

        if (!planMapper.isPlanKeyExist(modDTO.getPlanKey()))
            throw new CustomRuntimeException("존재 하지 않는 생산 계획입니다");

        if (!planMapper.isPlanAlreadyExecuted(modDTO.getPlanKey()))
            throw new CustomRuntimeException("이미 실행된 계획은 수정 할 수 없습니다");

        planMapper.updatePlan(modDTO);
    }
}
