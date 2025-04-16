package com.wimir.bae.domain.plan.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PlanModDTO {

    @NotBlank
    private String planKey;

    @NotBlank
    private String teamKey;

    private String teamCommonKey;
}
