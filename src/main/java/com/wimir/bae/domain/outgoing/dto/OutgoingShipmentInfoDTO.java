package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

@Data
public class OutgoingShipmentInfoDTO {

    private String outgoingKey;

    private String productKey;

    private String productCode;

    private String productName;

    private String warehouseKey;

    private String warehouseName;

    private String materialKey;

    private String contractCode;

    private String materialName;

    private String companyName;

    private String outgoingType;

    private String deliveryDate;

    private String outgoingDateTime;

    private String planQuantity;

    private String quantity;

    private String note;

    private String outsourceStatus;

    private String standardPrice;

    private int totalPrice;
}
