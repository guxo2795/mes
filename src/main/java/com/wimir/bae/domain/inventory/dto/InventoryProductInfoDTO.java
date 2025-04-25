package com.wimir.bae.domain.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class InventoryProductInfoDTO {

    private String productKey;

    private String productCode;

    private String productName;

    private String totalQuantity;

    private List<InventoryProductInfoDetailDTO> warehouse;

    private String unitName;
}
