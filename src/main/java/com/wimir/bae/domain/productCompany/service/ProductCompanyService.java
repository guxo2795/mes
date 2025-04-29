package com.wimir.bae.domain.productCompany.service;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import com.wimir.bae.domain.productCompany.mapper.ProductCompanyMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCompanyService {

    private final ProductCompanyMapper productCompanyMapper;
    private final ProductMapper productMapper;
    private final CompanyMapper companyMapper;

    public void createProductCompany(UserLoginDTO userLoginDTO, ProductCompanyRegDTO regDTO) {

        CompanyInfoDTO companyInfo = companyMapper.getCompanyInfo(regDTO.getCompanyKey());
        ProductInfoDTO productInfo = productMapper.getProductInfo(regDTO.getProductKey());

        // 품목, 업체 존재 확인
        if(productInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 품목입니다.");
        }
        if(companyInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 업체입니다.");
        }
        // 품목-업체 매핑 중복 확인
        if(productCompanyMapper.isProductCompanyExist(regDTO.getProductKey(), regDTO.getCompanyKey())){
            throw new CustomRuntimeException(
                    String.format("이미 등록된 품목-업체 매핑입니다. (품목: %s, 업체: %s)", productInfo.getProductCode(), companyInfo.getCompanyName()));
        }

        // 업체 조달,납부 확인
        String companyTypeFlag = companyInfo.getCompanyTypeFlag();
        String productAssetTypeFlag = productInfo.getAssetTypeFlag();

        if(companyTypeFlag.equals("O") && !productAssetTypeFlag.equals("M")){
            throw new CustomRuntimeException("조달 업체에는 자재만 등록할 수 있습니다.");
        } else if(companyTypeFlag.equals("S") && !productAssetTypeFlag.equals("F")){
            throw new CustomRuntimeException("납품 업체에는 완제품만 등록할 수 있습니다.");
        }

        // 매핑 생성
        productCompanyMapper.createProductCompany(regDTO);
    }
}
