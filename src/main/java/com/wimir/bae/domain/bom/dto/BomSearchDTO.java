package com.wimir.bae.domain.bom.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class BomSearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    private String record;

    private String sort;

    private String offset;

    private String productCode;

    private String productName;

    private String finishedKey;
}
