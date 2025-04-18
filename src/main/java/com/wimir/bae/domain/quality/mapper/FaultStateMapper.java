package com.wimir.bae.domain.quality.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FaultStateMapper {

    // 불량 현황 추가
    void createFaultState(@Param("processType") String processType,
                          @Param("processName") String processName,
                          @Param("keyCode") String keyCode,
                          @Param("faultDate") String faultDate,
                          @Param("faultCount") String faultCount);
}
