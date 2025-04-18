package com.wimir.bae.domain.planDetail.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PlanDetailQuantityModDTO {

    private String detailKey;

    @NotBlank
    private String executeQuantity;

    @NotBlank
    private String subExecuteQuantity;

    @NotBlank
    private String faultQuantity;

    @NotBlank
    private String executeDate;
}
