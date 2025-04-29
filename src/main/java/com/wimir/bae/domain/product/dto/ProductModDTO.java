package com.wimir.bae.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModDTO {

    @NotBlank
    private String productKey;

//    @NotBlank
//    private String companyKey;

    @NotBlank
    private String productTypeKey;

    @NotBlank
    private String unitKey;

    @NotBlank
    private String processTypeKey;

    @NotBlank
    private String drawingKey;

    @NotBlank
    @Size(max = 50)
    private String productCode;

    @NotBlank
    @Size(max = 50)
    private String productName;

    @NotBlank
    @Pattern(regexp = "^(0|[1-9]\\d*)$")
    private String standardPrice;

    @NotBlank
    @Pattern(regexp = "^[FM]$")
    private String assetTypeFlag;

    @NotBlank
    @Pattern(regexp = "^(0|[1-9]\\d*)$")
    private String safetyQuantity;

}
