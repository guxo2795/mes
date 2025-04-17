package com.wimir.bae.domain.plan.service;

import com.wimir.bae.domain.plan.dto.PlanContractInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanModDTO;
import com.wimir.bae.domain.plan.mapper.PlanMapper;
import com.wimir.bae.domain.planTotal.mapper.PlanTotalMapper;
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
public class PlanService {

    private final PlanMapper planMapper;
    private final PlanTotalMapper planTotalMapper;

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

    public void deletePlan(UserLoginDTO userLoginDTO, List<String> planKeyList) {

        for (String planKey : planKeyList) {

            if (!planMapper.isPlanKeyExist(planKey))
                throw new CustomRuntimeException("존재 하지 않는 생산 계획 입니다");

            if (!planMapper.isPlanAlreadyExecuted(planKey))
                throw new CustomRuntimeException("이미 실행된 계획은 삭제 할 수 없습니다.");

            planMapper.deletePlan(planKey);
        }
    }

    public void executedPlan(UserLoginDTO userLoginDTO, List<String> planKeyList) {
        for (String planKey : planKeyList) {

            // planKey에 대한 완제품, 자재 정보 리스트
            List<PlanContractInfoDTO> list = planMapper.getPlanContractInfoList(planKey, "12");

            if (!planMapper.isPlanKeyExist(planKey))
                throw new CustomRuntimeException("존재 하지 않는 생산 계획 입니다");

            if (!planMapper.isPlanAlreadyExecuted(planKey))
                throw new CustomRuntimeException("이미 실행된 계획 입니다.");

            // 팀이 연결되어 있는지 확인이 필요함 안되어 있으면 연결을 해야하고
            if (!planMapper.isPlanTeamExist(planKey)) {
                throw new CustomRuntimeException("팀이 연결 되어 있지 않습니다");
            }

            planMapper.executedPlan(planKey);

            for (PlanContractInfoDTO dto : list) {
                planTotalMapper.createResult(
                        planKey,
                        dto.getTeamKey(),
                        dto.getProductKey(),dto.getContractQuantity(),
                        "0",
                        "0",
                        dto.getContractStartDate());
            }

        }
    }
}
