package com.wimir.bae.domain.productCompany.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductCompanyRegDTO {

    @NotBlank
    private String productKey;

    @NotBlank
    private String companyKey;
}
