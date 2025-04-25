package com.wimir.bae.domain.quality.service;

import com.wimir.bae.domain.quality.dto.FaultEventInfoDTO;
import com.wimir.bae.domain.quality.mapper.FaultEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FaultEventService {

    private final FaultEventMapper faultEventMapper;

    @Transactional(readOnly = true)
    public List<FaultEventInfoDTO> getFaultEventList() {
        return Optional.ofNullable(faultEventMapper.getFaultEventList())
                .orElse(Collections.emptyList());
    }
}
