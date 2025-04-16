package com.wimir.bae.domain.contract.dto;

import lombok.Data;

@Data
public class MaterialsInfoDTO {

    private String contractCode;

    private String productKey;

    // 수량
    private String quantity;

    // 완제품 자재 및 자재 수량
    private String selectedQuantity;

    private String productCode;

    private String productName;

    private String assetTypeFlag;

    // 안전 재고
    private String safetyQuantity;

    private String isDeleted;
}
