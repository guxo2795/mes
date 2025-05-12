package com.wimir.bae.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoDTO {

    private String productKey;

    private String productCode;

    private String productName;

//    private String productImageKey;

//    private String productImagePath;

    private String assetTypeFlag;

    private String productTypeKey;

    private String productTypeName;

    private String processTypeKey;

    private String processTypeName;

    private String standardPrice;

    private String drawingKey;

    private String drawingName;

    private String safetyQuantity;

    private String unitKey;

    private String unitName;

//    private String companyKey;

//    private String companyName;

//    private String companyType;

    private String totalQuantity;
}
