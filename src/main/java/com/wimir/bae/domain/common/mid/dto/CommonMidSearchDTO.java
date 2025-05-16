package com.wimir.bae.domain.common.mid.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CommonMidSearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String record;

    private String sort;

    private String offset;

    private String midCommonName;
}
