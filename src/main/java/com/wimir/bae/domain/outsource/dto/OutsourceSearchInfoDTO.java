package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

@Data
public class OutsourceSearchInfoDTO {
    
    private String outsourceKey;
    
    private String contractCode;
    
    private String productKey;
    
    private String companyKey;
    
    private String quantity;

    // 출고 일자
    private String outboundDateTime;

    // 입고 예정일
    private String inboundEstDate;

    // 입하 확인일
    private String inboundDateTime;

    // 입고 일시
    private String incomingDateTime;

    // 외주 현황(P: 출하준비, O: 출하, C: 입고완료)
    private String outsourceState;

    private String outgoingUserKey;

    private String incomingUserKey;

    private String regDateTime;

    private String isDeleted;
}
