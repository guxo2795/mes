package com.wimir.bae.domain.productCompany.mapper;

import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;

@Mapper
public interface ProductCompanyMapper {

    boolean isProductCompanyExist(@Param("productKey") String productKey,
                                  @Param("companyKey") String companyKey);

    void createProductCompany(ProductCompanyRegDTO regDTO);
}
