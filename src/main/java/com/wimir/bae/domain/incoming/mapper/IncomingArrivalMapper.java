package com.wimir.bae.domain.incoming.mapper;

import com.wimir.bae.domain.incoming.dto.IncomingArrivalRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IncomingArrivalMapper {
    
    // 자재 입하 등록
    void createArrival(IncomingArrivalRegDTO incomingArrivalRegDTO);
}
