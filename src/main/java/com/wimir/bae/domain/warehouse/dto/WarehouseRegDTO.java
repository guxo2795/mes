package com.wimir.bae.domain.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseRegDTO {

    @NotBlank
    @Size(max = 50)
    private String warehouseName;

    @Size(max = 100)
    private String location;

    private String warehouseKey;
}
