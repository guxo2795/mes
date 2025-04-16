package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class OutgoingShipmentRegDTO {

    private String productKey;

    private String finishQuantity;

    @NotBlank
    private String contractKey;

    @NotBlank
    private String warehouseKey;

    // 감소 유형(M1: 자재 입하, M2: 자재 입고, M3 : 출하, M4: 출고, P: 생산, W1: 창고 재고 정정, W2: 창고 이동)
    @Pattern(regexp = "^(M1|M2|M3|M4|P|W1|W2)$")
    @NotBlank
    private String outgoingTypeFlag;

    // "출하 일시(년-월-일 시:분:초)
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    private String outgoingDateTime;

    @Size(max = 200)
    private String note;

    private String contractMaterialKey;

}
