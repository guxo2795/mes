package com.wimir.bae.domain.planTotal.dto;

import lombok.Data;

@Data
public class PlanTotalInfoDTO {

    private String contractCode;

    private String resultKey;

    private String planDate;

    private String companyName;

    private String teamName;

    private String contractName;

    private String productName;

    private String productCode;

    private String planQuantity;

    private String subExecuteQuantity;

    private String faultQuantity;

    private String contractDate;

    private String lastContractDate;

    private String deliveryDate;

    private String note;

    private String isCompleted;

    private String standardPrice;

    private int totalPrice;
}
