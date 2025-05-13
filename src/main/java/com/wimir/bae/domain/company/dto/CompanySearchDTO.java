package com.wimir.bae.domain.company.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CompanySearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String record;

    private String sort;

    private String offset;

    private String companyName;

    private String companyRegNumber;

    private String ceoName;

    private String address;

    private String note;

    private String companyTypeFlag;

}
