package com.wimir.bae.domain.company.service;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyModDTO;
import com.wimir.bae.domain.company.dto.CompanyProductsInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyRegDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
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
public class CompanyService {

    private final CompanyMapper companyMapper;

    public void createCompany(UserLoginDTO userLoginDTO, CompanyRegDTO regDTO) {

        // 업체명 중복 확인
        validateDuplicateCompanyName(regDTO.getCompanyName());

        companyMapper.createCompany(regDTO);
    }

    @Transactional(readOnly = true)
    public List<CompanyInfoDTO> getCompanyList() {
        return Optional.ofNullable(companyMapper.getCompanyList())
                .orElse(Collections.emptyList());
    }

    public void updateCompany(UserLoginDTO userLoginDTO, CompanyModDTO modDTO) {

        CompanyInfoDTO companyInfoDTO = companyMapper.getCompanyInfo(modDTO.getCompanyKey());
        if(companyInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 업체입니다.");
        }

        if (!companyInfoDTO.getCompanyName().equals(modDTO.getCompanyName())) {
            validateDuplicateCompanyName(modDTO.getCompanyName());
        }

        companyMapper.updateCompany(modDTO);
    }

    public void deleteCompany(UserLoginDTO userLoginDTO, List<String> companyKeyList) {
        List<CompanyInfoDTO> companyInfoDTOList = companyMapper.getCompanyInfoList(companyKeyList);

        if(companyInfoDTOList.size() != companyKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 업체가 포함되어 있습니다.");
        }
        // 업체 발주 여부 확인
        
        companyMapper.deleteCompanyList(companyKeyList);
    }

    @Transactional(readOnly = true)
    public List<CompanyProductsInfoDTO> getCompanyProductsList() {
        return Optional.ofNullable(companyMapper.getCompanyProductsList())
                .orElse(Collections.emptyList());
    }

    private void validateDuplicateCompanyName(String companyName) {
        if(companyMapper.isCompanyNameExist(companyName)) {
            throw new CustomRuntimeException("이미 존재하는 업체 명입니다.");
        }
    }
}
