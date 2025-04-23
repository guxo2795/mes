package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

@Data
public class OutsourceIncomingStateDTO {

    // 외주현황(P:출하준비, O:출하, C:입고완료)
    private String outsourceState;

    // 출하완료 및 입고완료 구분 플래그
    private String outsourceStatusResult;

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

    private String isCompleted;
}
