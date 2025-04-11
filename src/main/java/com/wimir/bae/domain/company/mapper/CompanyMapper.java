package com.wimir.bae.domain.company.mapper;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Mapper
public interface CompanyMapper {

    // 업체명 중복 확인 
    boolean isCompanyNameExist(String companyName);

    // 업체 등록
    void createCompany(CompanyRegDTO regDTO);

    // 업체 목록 조회
    List<CompanyInfoDTO> getCompanyList();
}
