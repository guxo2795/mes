package com.wimir.bae.domain.team.mapper;

import com.wimir.bae.domain.team.dto.StaffRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StaffMapper {
    
    // 생산팀 인원 등록
    void createStaff(StaffRegDTO regDTO);
}
