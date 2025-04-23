package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutsourceIncomingListDTO {

    // 수주 정보
    private OutsourceIncomingStateDTO contractInfo;

    // 외주 입고 예정 정보
    List<OutsourceIncomingDTO> outsourceIncomingList;
}
