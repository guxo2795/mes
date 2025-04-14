package com.wimir.bae.domain.company.mapper;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyModDTO;
import com.wimir.bae.domain.company.dto.CompanyProductsInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface CompanyMapper {

    // 업체명 중복 확인 
    boolean isCompanyNameExist(String companyName);

    // 업체 등록
    void createCompany(CompanyRegDTO regDTO);

    // 업체 목록 조회
    List<CompanyInfoDTO> getCompanyList();
    List<CompanyInfoDTO> getCompanyInfoList(List<String> companyKeyList);
    // 업체 정보
    CompanyInfoDTO getCompanyInfo(String companyKey);

    // 업체 수정
    void updateCompany(CompanyModDTO modDTO);

    // 업체 삭제
    void deleteCompanyList(List<String> companyKeyList);

    // 업체와 연결된 품목 목록 조회
    List<CompanyProductsInfoDTO> getCompanyProductsList();

    // 업체 종류 및 존재 유무 조회
    String getCompanyTypeFlag(String companyKey);
}
