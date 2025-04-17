package com.wimir.bae.domain.team.mapper;

import com.wimir.bae.domain.team.dto.TeamRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeamMapper {

    // 팀명 중복 확인
    boolean isTeamExist(String teamName);

    // 팀 공통코드 등록
    void createTeamCommon(TeamRegDTO regDTO);

    // 팀 공통코드 키
    String getSubCommonKeyByTeamName(String teamName);

    // 팀 등록
    void createTeam(@Param("subCommonKey") String subCommonKey);
}
