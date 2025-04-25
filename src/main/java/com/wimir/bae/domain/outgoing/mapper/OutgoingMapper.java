package com.wimir.bae.domain.outgoing.mapper;

import com.wimir.bae.domain.outgoing.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface OutgoingMapper {

    // 출하 중복 확인
    Boolean isShipmentDuplicateCheck(@Param("productKey") String productKey,
                                     @Param("materialKey") String materialKey);

    // 출하 등록
    void createShipment(OutgoingShipmentRegDTO outgoingShipmentRegDTO);

    // 출하 목록
    List<OutgoingShipmentInfoDTO> getOutgoingShipmentList();

    // 출하 일시 수정
    void updateOutgoing(OutgoingShipmentUpdateDTO outgoingShipmentUpdateDTO);

    // 출하 정보
    outgoingInfoDTO getOutgoingInfo(String outgoingKey);

    // 출하 삭제
    void deleteOutgoing(String outgoingKey);

    // 외주 종결 여부
    boolean isOutsourceComplete(@Param("contractCode") String contractCode,
                                @Param("processOutsourcedKey") String processOutsourcedKey);

    // 출하 완료
    void outgoingComplete(@Param("outgoingKey") String outgoingKey,
                          @Param("correctionDateTime") String correctionDateTime,
                          @Param("userCode") String userCode);

    // 출하 현황
    List<OutgoingShipmentEndInfoDTO> getOutgoingShipmentEndList();
}
