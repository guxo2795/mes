package com.wimir.bae.domain.plan.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateWarehouseDTO {

    @NotBlank
    private String planKey;

    @NotBlank
    private String productKey;

    @NotBlank
    private String warehouseKey;

}