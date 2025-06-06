package com.wimir.bae.domain.common.mid.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommonMidModDTO {

    private String midCommonKey;

    @Size(max = 50)
    private String midCommonName;

    @Size(max = 100)
    private String note;
}
