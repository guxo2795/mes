package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OutsourceIncomingCompleteRegDTO {

    @NotBlank
    private String warehouseKey;

    @NotBlank
    private String outsourceKey;

    private String userCode;
}
