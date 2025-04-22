package com.wimir.bae.domain.incoming.mapper;

import com.wimir.bae.domain.incoming.dto.IncomingArrivalInfoDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalRegDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IncomingArrivalMapper {
    
    // 자재 입하 등록
    void createArrival(IncomingArrivalRegDTO incomingArrivalRegDTO);

    // 자재 입고/입하 목록
    List<IncomingArrivalInfoDTO> getArrivalList(IncomingArrivalSearchDTO incomingArrivalSearchDTO);
}
