package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OutsourceUpdateDTO {

    @NotBlank
    private String outsourceKey;

    // 입고 예정일(년-월-일)
    @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String inboundEstDate;

    // 출고 일자(년-월-일 시:분:초
    @Pattern(regexp = "^$|^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    private String outboundDateTime;

    private String quantity;
}
