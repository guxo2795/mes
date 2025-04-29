package com.wimir.bae.domain.productCompany.mapper;

import com.wimir.bae.domain.productCompany.dto.ProductCompanyFlatDTO;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyInfoDTO;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyModDTO;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface ProductCompanyMapper {

    boolean isProductCompanyExist(@Param("productKey") String productKey,
                                  @Param("companyKey") String companyKey);

    void createProductCompany(ProductCompanyRegDTO regDTO);

    List<ProductCompanyFlatDTO> getProductCompanyList();

    List<ProductCompanyFlatDTO> getProductCompanyInfoList(List<String> productCompanyKeyList);

    ProductCompanyFlatDTO getProductCompanyInfo(String productCompanyKey);

    void updateProductCompany(ProductCompanyModDTO modDTO);

    void deleteProductCompanyList(List<String> productCompanyKeyList);
}
