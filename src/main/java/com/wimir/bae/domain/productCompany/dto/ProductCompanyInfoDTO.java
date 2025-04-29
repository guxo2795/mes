package com.wimir.bae.domain.productCompany.dto;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductCompanyInfoDTO {

    private String productKey;

    private String productCode;

    private String productName;

    private String assetTypeFlag;

    private List<ProductCompanyInfoDetailDTO> companyList;

}
