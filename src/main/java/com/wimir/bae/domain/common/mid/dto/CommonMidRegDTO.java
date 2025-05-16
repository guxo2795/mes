package com.wimir.bae.domain.common.mid.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommonMidRegDTO {

    @NotBlank
    private String mainCommonKey;

    @NotBlank
    @Size(max = 50)
    private String midCommonName;

    @Size(max = 200)
    private String note;

}
