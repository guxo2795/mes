package com.wimir.bae.domain.outgoing.mapper;

import com.wimir.bae.domain.outgoing.dto.OutgoingShipmentRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OutgoingMapper {

    // 출하 중복 확인
    Boolean isShipmentDuplicateCheck(@Param("productKey") String productKey,
                                     @Param("materialKey") String materialKey);

    // 출하 등록
    void createShipment(OutgoingShipmentRegDTO outgoingShipmentRegDTO);

}
