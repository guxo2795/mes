package com.wimir.bae.domain.plan.dto;

import lombok.Data;

@Data
public class PlanTotalSearchDTO {

    private String resultKey;

    private String planKey;

    private String teamKey;

    private String productKey;

    private String warehouseKey;

    private int planQuantity;

    private int executeQuantity;

    private int faultQuantity;

    private String contractCompleteDate;

    private String lastContractDate;

    private String note;

    private String isCompleted;
}
