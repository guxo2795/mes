package com.wimir.bae.domain.plan.dto;

import lombok.Data;

@Data
public class PlanInfoDTO {

    private String planKey;

    private String teamCommonKey;

    private String teamKey;

    private String contractName;

    private String contractCode;

    private String isExecuted;

    private String planDate;

    private String deliveryDate;

    private String teamName;

    private String productName;

    private String productCode;

    private String planQuantity;

    private String subPlanQuantity;

    private String productionRate;

    private String faultQuantity;

    private String faultRate;

    private String CompanyName;

    private String rowColor;
}
