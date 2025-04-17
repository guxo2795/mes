package com.wimir.bae.domain.planTotal.service;

import com.wimir.bae.domain.planTotal.mapper.PlanTotalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanTotalService {

    private final PlanTotalMapper planTotalmapper;
}
