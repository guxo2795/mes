package com.wimir.bae.domain.company.dto;

import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProductsInfoDTO {

    private String companyKey;

    private String companyName;

    private List<ProductInfoDTO> productList;

}
