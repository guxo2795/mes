package com.wimir.bae.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class OrderItemInfoDTO {

    // 발주 품목 키
    private String orderMaterialKey;

    // 자재 키
    private String materialKey;

    // 자재 품번
    private String materialCode;

    // 자재 품명
    private String materialName;

    // 발주 수량
    private String orderQuantity;
    
    // 입고 가능 수량
    private String arrivedQuantity;

    // 발주 입고 수량
    private String inboundedQuantity;

    // 제품 단가
    private int standardPrice;

    // 입고율
    private String orderInboundRate;

    // 단위
    private String unitName;
    
    // 발주 키
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderKey;
}
