package com.wimir.bae.domain.productCompany.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductCompanyInfoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productKey;

    private String productCode;

    private String productName;

    private String assetTypeFlag;

    private List<ProductCompanyInfoDetailDTO> companyList;

}
