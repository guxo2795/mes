package com.wimir.bae.domain.quality.mapper;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import com.wimir.bae.domain.quality.dto.FaultStateModDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
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

    // 불량 현황 존재 유무
    boolean isFaultStateExist(String faultStateKey);

    // 불량 현황 수정
    void updateFaultState(FaultStateModDTO modDTO);
}
