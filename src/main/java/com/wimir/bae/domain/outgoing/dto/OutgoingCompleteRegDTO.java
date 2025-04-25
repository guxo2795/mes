package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OutgoingCompleteRegDTO {

    @NotBlank
    private String outgoingKey;

    // 수주 품목(완제품) 키
    @NotBlank
    private String materialKey;

    @NotBlank
    private String warehouseKey;

    @NotBlank
    private String productKey;

    private double outgoingQuantity;
}
