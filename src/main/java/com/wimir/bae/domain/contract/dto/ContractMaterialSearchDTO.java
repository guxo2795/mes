package com.wimir.bae.domain.contract.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ContractMaterialSearchDTO {

    @NotBlank
    private String contractCode;
}
