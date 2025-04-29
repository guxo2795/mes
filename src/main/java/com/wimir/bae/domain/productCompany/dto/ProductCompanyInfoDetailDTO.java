package com.wimir.bae.domain.productCompany.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProductCompanyInfoDetailDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String companyKey;

    private String companyName;

    private String companyTypeFlag;
}
