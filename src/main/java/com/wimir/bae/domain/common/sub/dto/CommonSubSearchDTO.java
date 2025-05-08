package com.wimir.bae.domain.common.sub.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CommonSubSearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String record;

    public String offset;

    private String subCommonName;

}