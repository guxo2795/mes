package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

@Data
public class IncomingMaterialInfoDTO {

    private String userName;

    private String incomingTypeFlag;

    private String productCode;

    private String productName;

    private String warehouseName;

    private String incomingDateTime;

    private String quantity;

    private String unitName;

    private String standardPrice;

    private int totalPrice;

    private String note;

    private String regDateTime;
}
