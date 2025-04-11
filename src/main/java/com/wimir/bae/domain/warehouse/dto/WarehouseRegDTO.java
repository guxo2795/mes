package com.wimir.bae.domain.warehouse.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class WarehouseRegDTO {

    @NotBlank
    @Size(max = 50)
    private String warehouseName;

    @Size(max = 100)
    private String location;

    private String warehouseKey;
}
