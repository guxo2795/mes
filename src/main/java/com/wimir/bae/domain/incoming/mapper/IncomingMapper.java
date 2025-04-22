package com.wimir.bae.domain.incoming.mapper;

import com.wimir.bae.domain.incoming.dto.IncomingMaterialInfoDTO;
import com.wimir.bae.domain.incoming.dto.IncomingMaterialSearchDTO;
import com.wimir.bae.domain.incoming.dto.IncomingQuantityDTO;
import com.wimir.bae.domain.incoming.dto.IncomingRegDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IncomingMapper {

    // 자재 발주 품목 수량 합계
    IncomingQuantityDTO getQuantitySum(String orderMaterialKey);

    // 자재 입고 등록
    void createIncoming(IncomingRegDTO incomingRegDTO);

    // 자재 입고/입하 현황
    List<IncomingMaterialInfoDTO> getIncomingMaterialList(IncomingMaterialSearchDTO incomingMaterialSearchDTO);
}
