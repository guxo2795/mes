package com.wimir.bae.domain.product.mapper;

import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.dto.ProductModDTO;
import com.wimir.bae.domain.product.dto.ProductRegDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    
    // 품번 중복 검사
    boolean isProductExist(String productCode);

    // 품목 등록
    void createProduct(ProductRegDTO regDTO);

    // 품목 목록 조회
    List<ProductInfoDTO> getProductList();
    List<ProductInfoDTO> getProductInfoList(List<String> productKeyList);

    // 품목 정보
    ProductInfoDTO getProductInfo(String productKey);

    // 품목 수정
    void updateProduct(ProductModDTO modDTO);

    // 품목 삭제
    void deleteProduct(String productKey);
    void deleteProductList(List<String> productKeyList);

    // 특정 업체의 품목 목록 조회
    List<ProductInfoDTO> getCompanyProductList(String companyKey);
}
