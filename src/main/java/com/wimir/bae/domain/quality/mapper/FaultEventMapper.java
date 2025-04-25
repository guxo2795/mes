package com.wimir.bae.domain.quality.mapper;

import com.wimir.bae.domain.quality.dto.FaultEventInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaultEventMapper {

    // 불량 내역 조회
    List<FaultEventInfoDTO> getFaultEventList();
}
