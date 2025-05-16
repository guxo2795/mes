package com.wimir.bae.domain.common.mid.dto;

import lombok.Data;

@Data
public class CommonMidInfoDTO {

    private String midCommonKey;

    private String midCommonName;

    private String note;

    private String isImmutable;

    private String mainCommonKey;
}
