package com.wimir.bae.domain.company.service;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyRegDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
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
        if(companyMapper.isCompanyNameExist(regDTO.getCompanyName())) {
            throw new RuntimeException();
        }
        
        companyMapper.createCompany(regDTO);
    }

    @Transactional(readOnly = true)
    public List<CompanyInfoDTO> getCompanyList() {
        return Optional.ofNullable(companyMapper.getCompanyList())
                .orElse(Collections.emptyList());
    }
}
