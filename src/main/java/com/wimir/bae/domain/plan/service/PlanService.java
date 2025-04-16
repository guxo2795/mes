package com.wimir.bae.domain.plan.service;

import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
