package com.wimir.bae.domain.plan.dto;

import lombok.Data;

@Data
public class PlanContractInfoDTO {

    private String planKey;

    private String contractName;

    private String productKey;

    private String teamKey;

    private String productCode;

    private String productName;

    private String keyCode;

    private String contractQuantity;

    private String contractStartDate;

    private String deliveryDate;

    private String warehouseKey;

    private String isCompleted;
}
