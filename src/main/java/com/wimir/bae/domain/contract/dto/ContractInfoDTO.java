package com.wimir.bae.domain.contract.dto;

import lombok.Data;

@Data
public class ContractInfoDTO {

    private String contractCode;

    private String contractName;

    private String companyKey;

    private String companyName;

    private String contractDate;

    private String deliveryDate;

    private String productKey;

    private String quantity;

    private String productCode;

    private String productName;

    private String unitName;

    private String assetTypeFlag;

    private String safetyQuantity;

    private String isDeleted;

    private String isCompleted;
}
