package com.wimir.bae.domain.planTotal.service;

import com.wimir.bae.domain.planTotal.dto.PlanTotalInfoDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalModDTO;
import com.wimir.bae.domain.planTotal.mapper.PlanTotalMapper;
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

    @Transactional(readOnly = true)
    public List<PlanTotalInfoDTO> getResultList() {
        return Optional.ofNullable(planTotalmapper.getResultList())
                .orElse(Collections.emptyList());
    }

    public void updateResultNote(PlanTotalModDTO modDTO) {
        planTotalmapper.updateResultNote(modDTO);
    }
}
