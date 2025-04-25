package com.wimir.bae.domain.quality.service;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import com.wimir.bae.domain.quality.mapper.FaultStateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FaultStateService {

    private final FaultStateMapper faultStateMapper;

    @Transactional(readOnly = true)
    public List<FaultStateDTO> getFaultStateList() {
        return Optional.ofNullable(faultStateMapper.getFaultStateList())
                .orElse(Collections.emptyList());
    }
}
