package com.wimir.bae.domain.planTotal.dto;

import lombok.Data;

@Data
public class PlanTotalResultDTO {

    private String planKey;

    private String productKey;

    private String planQuantity;

    private String executeQuantity;

    private String contractMaterialKey;

    private String warehouseKey;

    private String warehouseName;

    private String contractKey;
}
