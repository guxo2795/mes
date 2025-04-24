package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

@Data
public class outgoingInfoDTO {

    private String contractName;

    private String outgoingKey;

    private String productKey;

    private String warehouseKey;

    private String materialKey;

    private String outgoingTypeFlag;

    private String outgoingDateTime;

    private String correctionDateTime;

    private String quantity;

    private String note;

    private String regDateTime;

    private String isCompleted;

    private String isDeleted;
}
