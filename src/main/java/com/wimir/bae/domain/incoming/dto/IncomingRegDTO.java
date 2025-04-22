package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IncomingRegDTO {

    private String userCode;

    // 품목키(입고에는 필요x)
    private String productKey;

    @NotBlank
    private String warehouseKey;

    // 발주 품목 키(입고, 입하 시 필요)
    private String orderMaterialKey;

    // 입고 유형 (M1: 자재 입하, M2: 자재 입고, P: 생산, W1: 창고 재고 정정, W2: 창고 이동)
    @NotBlank
    @Pattern(regexp = "^(M1|M2|P|W1|W2)$")
    private String IncomingTypeFlag;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    private String incomingDateTime;

    private String executeDate;

    private String correctionDateTime;

    private String quantity;

    private String note;

}
