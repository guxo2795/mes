package com.wimir.bae.domain.contract.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ContractRegDTO {

    private String contractCode;

    @NotBlank
    private String companyKey;

    @NotBlank
    @Size(max = 50)
    private String contractName;

    @NotBlank
    private String productKey;

    @NotBlank
    private String quantity;

    @NotBlank
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String contractDate;

    @NotBlank
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String deliveryDate;
}
