package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

@Data
public class IncomingArrivalInfoDTO {

    private String productCode;

    private String productName;

    private String warehouseName;

    private String quantity;

    private String unitName;

    private String incomingDateTime;

    private String userName;
}
