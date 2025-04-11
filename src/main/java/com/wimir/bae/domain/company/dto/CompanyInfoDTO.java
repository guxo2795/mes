package com.wimir.bae.domain.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfoDTO {

    private String companyKey;

    private String companyTypeFlag;

    private String companyName;

    private String companyRegNumber;

    private String ceoName;

    private String address;

    private String note;

}