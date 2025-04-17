package com.wimir.bae.domain.team.service;

import com.wimir.bae.domain.team.dto.TeamRegDTO;
import com.wimir.bae.domain.team.mapper.StaffMapper;
import com.wimir.bae.domain.team.mapper.TeamMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;
    private final StaffMapper staffMapper;

    public void createTeam(TeamRegDTO regDTO, UserLoginDTO userLoginDTO) {

        if(teamMapper.isTeamExist(regDTO.getTeamName())){
            throw new CustomRuntimeException("이미 존재하는 팀명입니다,");
        }

        teamMapper.createTeamCommon(regDTO);

        teamMapper.createTeam(teamMapper.getSubCommonKeyByTeamName(regDTO.getTeamName()));
    }
}
