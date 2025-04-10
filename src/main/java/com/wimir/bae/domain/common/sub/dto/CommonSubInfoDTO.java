package com.wimir.bae.domain.common.sub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonSubInfoDTO {

    private String subCommonKey;

    private String subCommonName;

    private String note;

    private String isImmutable;

    private String mainCommonKey;

}
