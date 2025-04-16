package com.wimir.bae.domain.contract.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContractMaterialDeleteDTO {

    private List<String> contractMaterialKeyList;

    private String contractCode;
}
