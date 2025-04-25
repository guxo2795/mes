package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

@Data
public class OutgoingShipmentEndInfoDTO {

    private String outgoingKey;

    private String productKey;

    private String productCode;

    private String productName;

    private String warehouseKey;

    private String warehouseName;

    private String materialKey;

    private String materialName;

    private String companyName;

    private String outgoingType;

    private String deliveryDate;

    private String outgoingDateTime;

    private String quantity;

    private String note;

    // 최종 출하 확인자
    private String userName;

    private String standardPrice;

    private int totalPrice;
}
