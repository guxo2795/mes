package com.wimir.bae.domain.plan.dto;

import lombok.Data;

@Data
public class PlanWarehouseDTO {

    private String productKey;

    private String warehouseKey;

    private String warehouseName;

    private String location;

    private String quantity;

}
