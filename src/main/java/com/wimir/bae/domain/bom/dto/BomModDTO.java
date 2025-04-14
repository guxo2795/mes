package com.wimir.bae.domain.bom.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class BomModDTO {

    @NotBlank
    private String bomKey;

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$")
    private String quantity;

}
