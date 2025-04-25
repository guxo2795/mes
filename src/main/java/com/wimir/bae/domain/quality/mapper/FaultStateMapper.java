package com.wimir.bae.domain.quality.mapper;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaultStateMapper {

    // 불량 현황 추가
    void createFaultState(@Param("processType") String processType,
                          @Param("processName") String processName,
                          @Param("keyCode") String keyCode,
                          @Param("faultDate") String faultDate,
                          @Param("faultCount") String faultCount);

    // 불량 현황 조회
    List<FaultStateDTO> getFaultStateList();
}
