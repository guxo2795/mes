package com.wimir.bae.domain.inventory.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class InventoryProductInoutMoveDTO {

    @NotBlank
    private String productKey;

    @NotBlank
    private String warehouseKey;

    @NotBlank
    private String newWarehouseKey;

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$")
    private String quantity;

    @NotBlank
    @Size(max = 200)
    private String note;

    private String correctionDateTime;
}
