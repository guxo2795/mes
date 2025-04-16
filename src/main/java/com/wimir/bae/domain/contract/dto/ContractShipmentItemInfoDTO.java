package com.wimir.bae.domain.contract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ContractShipmentItemInfoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contractCode;
    
    private String contractMaterialKey;
    
    private String productKey;
    
    private String productCode;
    
    private String productName;
    
    private String assetTypeFlag;

    // 수주 수량
    private String contractQuantity;

    // 출고 가능 수량
    private String arrivedQuantity;

    // 수주 출고 수량
    private String outboundQuantity;

    private String unitName;
}
