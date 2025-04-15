package com.wimir.bae.domain.contract.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ContractModDTO {

    private String contractCode;

    private String companyKey;

    private String contractName;

    private String productKey;

    private String quantity;

    // 실행 여부 flag(0 : 미실행, 1 : 완료, 2 : 실행중)
    private String isCompleted;

    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String contractDate;

    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String deliveryDate;
}
