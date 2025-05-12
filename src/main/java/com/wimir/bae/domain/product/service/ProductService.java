package com.wimir.bae.domain.product.service;


import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.contract.service.ContractService;
import com.wimir.bae.domain.inventory.service.InventoryProductService;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.dto.ProductModDTO;
import com.wimir.bae.domain.product.dto.ProductRegDTO;
import com.wimir.bae.domain.product.dto.ProductSearchDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import com.wimir.bae.domain.productCompany.service.ProductCompanyService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.utils.PagingUtil;
import com.wimir.bae.global.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductMapper productMapper;
//    private final CompanyMapper companyMapper;

    private final InventoryProductService inventoryProductService;
    private final ProductCompanyService productCompanyService;

    @Value("${bae.company.wimir}")
    private String WIMIR;
    @Value("${bae.process.inHouse}")
    private String inHouse;

    // 품목 생성
    public void createProduct(UserLoginDTO userLoginDTO, ProductRegDTO regDTO) {
        
        // 품번 중복 확인
        validateProductCode(regDTO.getProductCode());

//        // 업체 조달, 납부 확인
//        CompanyInfoDTO companyInfo = companyMapper.getCompanyInfo(regDTO.getCompanyKey());
//        if (companyInfo == null) {
//            throw new CustomRuntimeException("존재하지 않는 거래처입니다. 존재하는 거래처를 선택 후 재시도 해주세요.");
//        }
//        String companyTypeFlag = companyInfo.getCompanyTypeFlag();
//        String newAssetTypeFlag = regDTO.getAssetTypeFlag();
//
//        if (companyTypeFlag.equals("O") && !newAssetTypeFlag.equals("M")) {
//            throw new CustomRuntimeException("조달 업체에는 자재만 등록할 수 있습니다.");
//        } else if (companyTypeFlag.equals("S") && !newAssetTypeFlag.equals("F")) {
//            throw new CustomRuntimeException("납품 업체에는 완제품만 등록할 수 있습니다.");
//        }
        
        // 품목 생성
        productMapper.createProduct(regDTO);
        
        // 사내생산일 경우 업체 자동으로 WIMIR 매핑
        if(inHouse.equals(regDTO.getProcessTypeKey())) {
            ProductCompanyRegDTO productCompanyRegDTO = new ProductCompanyRegDTO();
            productCompanyRegDTO.setProductKey(regDTO.getProductKey());
            productCompanyRegDTO.setCompanyKey(WIMIR);
            productCompanyService.createProductCompany(userLoginDTO, productCompanyRegDTO);
        }

        // 품목 등록 시 초기 재고 설정
        inventoryProductService.setInitialInventoryByProduct(regDTO.getProductKey());
    }

    // 품목 목록 조회
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> getProductList(ProductSearchDTO searchDTO) {

        searchDTO.setOffset(
                PagingUtil.getPagingOffset(
                        searchDTO.getPage(),
                        searchDTO.getRecord()
                )
        );

        searchDTO.setSort(SortUtil.getDBSortStr(searchDTO.getSort()));

        return Optional.ofNullable(productMapper.getProductList(searchDTO))
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
