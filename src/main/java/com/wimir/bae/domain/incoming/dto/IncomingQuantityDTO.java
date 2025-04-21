package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

@Data
public class IncomingQuantityDTO {

    // 입하 수량
    private double arrivedQuantity;

    // 입고 수량
    private double inboundedQuantity;
}
