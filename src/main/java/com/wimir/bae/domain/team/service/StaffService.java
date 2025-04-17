package com.wimir.bae.domain.team.service;

import com.wimir.bae.domain.team.dto.StaffRegDTO;
import com.wimir.bae.domain.team.mapper.StaffMapper;
import com.wimir.bae.domain.team.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {

    private final StaffMapper staffMapper;
    private final TeamMapper teamMapper;

    public void createStaff(StaffRegDTO regDTO) {

        staffMapper.createStaff(regDTO);
    }
}
