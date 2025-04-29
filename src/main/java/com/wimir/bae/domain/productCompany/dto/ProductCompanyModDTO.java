package com.wimir.bae.domain.productCompany.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductCompanyModDTO {

    @NotBlank
    private String productCompanyKey;

    @NotBlank
    private String productKey;

    @NotBlank
    private String companyKey;
}
