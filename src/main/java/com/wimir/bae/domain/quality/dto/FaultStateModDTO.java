package com.wimir.bae.domain.quality.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class FaultStateModDTO {

    @NotBlank
    private String faultStateKey;

    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String faultDate;

    @NotBlank
    @Pattern(regexp="^(?!-)(\\d+|\\d+\\.\\d{1,3})$")
    private String faultCount;
}
