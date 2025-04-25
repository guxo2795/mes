package com.wimir.bae.domain.outgoing.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class OutgoingDecreaseRegDTO {

    @NotBlank
    private String productKey;

    @NotBlank
    private String warehouseKey;

    // 감소 유형(M1: 자재 입하, M2: 자재 입고, M3 : 출하, M4: 출고, P: 생산, W1: 창고 재고 정정, W2: 창고 이동)
    @Pattern(regexp = "^(M1|M2|M3|M4|P|W1|W2)$")
    @NotBlank
    private String outgoingTypeFlag;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    @NotBlank
    private String correctionDateTime;

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$")
    private String quantity;

    @Size(max = 200)
    private String note;
}
