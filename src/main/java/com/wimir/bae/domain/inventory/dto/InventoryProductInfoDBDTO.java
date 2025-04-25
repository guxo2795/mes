package com.wimir.bae.domain.inventory.dto;

import lombok.Data;

@Data
public class InventoryProductInfoDBDTO {

    private String productKey;

    private String productCode;

    private String productName;

    private String totalQuantity;

    private String warehouseKey;

    private String warehouseName;

    private String quantity;

    private String unitName;
}
