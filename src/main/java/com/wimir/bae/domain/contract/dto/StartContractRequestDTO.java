package com.wimir.bae.domain.contract.dto;

import lombok.Data;

import java.util.List;

@Data
public class StartContractRequestDTO {

    private String contractCode;

    private String planDate;

    private List<ContractWarehouseDTO> warehouseKeyMap;
}
