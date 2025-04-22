package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class IncomingArrivalRegDTO {
    
    // 반환용
    private String userCode;
    private String productKey;

    @NotBlank
    private String orderMaterialKey;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    private String incomingDateTime;

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$")
    private String quantity;

    @Size(max = 200)
    private String note;
}