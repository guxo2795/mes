package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

@Data
public class OutsourceIncomingDTO {

    private String contractCode;

    private String outsourceKey;

    private String companyKey;

    private String companyName;

    private String quantity;

    private String outgoingCompleteDate;

    private String inboundDateTime;

    private String incomingEstDate;

    private String incomingDateTime;

    private String outgoingUserKey;

    private String incomingUserKey;

    private String outsourceState;

    private String productKey;

    private String productName;

    private String productCode;
}
