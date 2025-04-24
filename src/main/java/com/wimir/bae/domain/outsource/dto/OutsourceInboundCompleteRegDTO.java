package com.wimir.bae.domain.outsource.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OutsourceInboundCompleteRegDTO {

    @NotBlank
    private String outsourceKey;
}
