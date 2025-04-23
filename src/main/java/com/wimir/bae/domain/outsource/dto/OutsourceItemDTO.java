package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

@Data
public class OutsourceItemDTO {

    private String productKey;

    private String productCode;

    private String productName;

    private String productImageKey;

    private String imagePath;

    private String companyKey;

    private String companyName;

    private String quantity;

    private String outsourceState;

    private String standardPrice;

    private int totalPrice;
}
