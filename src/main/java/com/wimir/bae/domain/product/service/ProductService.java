package com.wimir.bae.domain.product.service;


import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.dto.ProductModDTO;
import com.wimir.bae.domain.product.dto.ProductRegDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final  ProductMapper productMapper;

    // 품목 생성
    public void createProduct(UserLoginDTO userLoginDTO, ProductRegDTO regDTO) {
        
        // 품번 중복 확인
        validateProductCode(regDTO.getProductCode());
        
        // 품목 생성
        productMapper.createProduct(regDTO);
    }

    // 품목 목록 조회
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> getProductList() {
        return Optional.ofNullable(productMapper.getProductList())
                .orElse(Collections.emptyList());
    }

    // 품목 수정
    public void updateProduct(UserLoginDTO userLoginDTO, ProductModDTO modDTO) {
        
        ProductInfoDTO productInfoDTO = productMapper.getProductInfo(modDTO.getProductKey());

        // 품목 존재 여부 확인
        validateProductExists(productInfoDTO);
        
        // 품번 중복 확인
        String oldProductCode = productInfoDTO.getProductCode();
        String newProductCode = modDTO.getProductCode();
        if(!oldProductCode.equals(newProductCode)) {
            validateProductCode(newProductCode);
        }

        productMapper.updateProduct(modDTO);

    }

    // 품목 삭제
    public void deleteProduct(UserLoginDTO userLoginDTO, List<String> productKeyList) {

//        for(String productKey : productKeyList) {
//            ProductInfoDTO productInfoDTO = productMapper.getProductInfo(productKey);
//
//            // 품목 존재 여부 확인
//            validateProductExists(productInfoDTO);
//
//            productMapper.deleteProduct(productKey);
//
//        }

        List<ProductInfoDTO> productInfoDTOList = productMapper.getProductInfoList(productKeyList);

        // 유효성 검증
        if(productInfoDTOList.size() != productKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 품목이 포함되어 있습니다.");
        }

        productMapper.deleteProductList(productKeyList);
        
    }

    private void validateProductExists(ProductInfoDTO productInfoDTO) {
        if(productInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 품목입니다. 다시 확인해 주세요.");
        }
    }

    private void validateProductCode(String ProductCode) {
        if(productMapper.isProductExist(ProductCode)) {
            throw new CustomRuntimeException("이미 존재하는 품번입니다.");
        }
    }
}
