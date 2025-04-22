package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IncomingArrivalSearchDTO {

    @NotBlank
    private String orderMaterialKey;

    // 입고 유형(M1: 자재 입하, M2: 자재 입고)
    @Pattern(regexp = "^(M1|M2)?$")
    private String incomingTypeFlag;
}
