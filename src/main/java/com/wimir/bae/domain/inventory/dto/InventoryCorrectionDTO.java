package com.wimir.bae.domain.inventory.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class InventoryCorrectionDTO {

    @NotBlank
    private String productKey;

    @NotBlank
    private String warehouseKey;

    @NotBlank
    @Pattern(regexp = "^(0|[1-9]\\d*)(\\.\\d+)?$")
    private String quantity;

    @Size(max = 200)
    private String note;
}
