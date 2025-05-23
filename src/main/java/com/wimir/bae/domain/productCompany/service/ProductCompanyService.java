package com.wimir.bae.domain.productCompany.service;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.productCompany.dto.*;
import com.wimir.bae.domain.productCompany.mapper.ProductCompanyMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<ProductCompanyInfoDTO> getProductCompanyList() {

        List<ProductCompanyFlatDTO> flatList =  Optional.ofNullable(productCompanyMapper.getProductCompanyList())
                .orElse(Collections.emptyList());

        Map<String, List<ProductCompanyInfoDetailDTO>> companyMap = flatList.stream()
                .collect(Collectors.groupingBy(ProductCompanyFlatDTO::getProductCode,
                        Collectors.mapping(dto -> {
                            ProductCompanyInfoDetailDTO companyDTO = new ProductCompanyInfoDetailDTO();
                            companyDTO.setCompanyName(dto.getCompanyName());
                            companyDTO.setCompanyTypeFlag(dto.getCompanyTypeFlag());
                            return companyDTO;
                        }, Collectors.toList())));

        return flatList.stream()
                .map(flatDTO -> {
                    ProductCompanyInfoDTO resultDTO = new ProductCompanyInfoDTO();
                    resultDTO.setProductCode(flatDTO.getProductCode());
                    resultDTO.setProductName(flatDTO.getProductName());
                    resultDTO.setAssetTypeFlag(flatDTO.getAssetTypeFlag());
                    resultDTO.setCompanyList(companyMap.get(flatDTO.getProductCode()));
                    return resultDTO;
                })
                .distinct()
                .toList();
    }

    // update 기능은 딱히 필요없어보임
    public void updateProductCompany(UserLoginDTO userLoginDTO, ProductCompanyModDTO modDTO) {

        ProductCompanyFlatDTO productCompanyInfo = productCompanyMapper.getProductCompanyInfo(modDTO.getProductCompanyKey());
        if (productCompanyInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 품목-업체 매핑입니다.");
        }

        CompanyInfoDTO companyInfo = companyMapper.getCompanyInfo(modDTO.getCompanyKey());
        ProductInfoDTO productInfo = productMapper.getProductInfo(modDTO.getProductKey());
        if(productInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 품목입니다.");
        }
        if(companyInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 업체입니다.");
        }
//
//        // 업체가 변경될 경우, 기존 품목의 진행중인 수주, 발주 확인
//        String oldCompanyKey = productCompanyInfo.getCompanyKey();
//        String newCompanyKey = modDTO.getCompanyKey();
//        if(!oldCompanyKey.equals(newCompanyKey)){
//            checkActiveOrderAndContract(productCompanyInfo.getProductKey());
//        }

        if(!(productCompanyInfo.getProductKey().equals(productInfo.getProductKey()) &&
        productCompanyInfo.getCompanyKey().equals(companyInfo.getCompanyKey()))) {
            if(productCompanyMapper.isProductCompanyExist(productInfo.getProductKey(), companyInfo.getCompanyKey())){
                throw new CustomRuntimeException(
                        String.format("이미 등록된 품목-업체 매핑입니다. (품목: %s, 업체: %s)", productInfo.getProductCode(), companyInfo.getCompanyName()));
            }
        }

        String companyTypeFlag = companyInfo.getCompanyTypeFlag();
        String productAssetTypeFlag = productInfo.getAssetTypeFlag();
        if(companyTypeFlag.equals("O") && !productAssetTypeFlag.equals("M")){
            throw new CustomRuntimeException("조달 업체에는 자재만 등록할 수 있습니다.");
        } else if(companyTypeFlag.equals("S") && !productAssetTypeFlag.equals("F")){
            throw new CustomRuntimeException("납품 업체에는 완제품만 등록할 수 있습니다.");
        }

        productCompanyMapper.updateProductCompany(modDTO);
    }

    public void deleteProductCompany(UserLoginDTO userLoginDTO, List<String> productCompanyKeyList) {
        List<ProductCompanyFlatDTO> productCompanyInfoDTOList = productCompanyMapper.getProductCompanyInfoList(productCompanyKeyList);
        if(productCompanyInfoDTOList.size() != productCompanyKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 품목-업체 매핑이 포함되어 있습니다.");
        }
        List<String> inOrderKeys = productCompanyMapper.getProductCompanyKeysInOrder(productCompanyKeyList);
        if(!inOrderKeys.isEmpty()){
            throw new CustomRuntimeException("발주 중인 품목-업체 매핑이 있어 삭제할 수 없습니다: " + String.join(", ",inOrderKeys));
        }
        List<String> inOrderContractKeys = productCompanyMapper.getProductCompanyKeysInOrderContract(productCompanyKeyList);
        if(!inOrderContractKeys.isEmpty()){
            throw new CustomRuntimeException("수주 중인 품목-업체 매핑이 있어 삭제할 수 없습니다: " + String.join(", ",inOrderContractKeys));
        }
        productCompanyMapper.deleteProductCompanyList(productCompanyKeyList);
    }
}
