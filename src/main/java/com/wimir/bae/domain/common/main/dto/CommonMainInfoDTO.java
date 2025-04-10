package com.wimir.bae.domain.common.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonMainInfoDTO {

    private String mainCommonKey;

    private String mainCommonName;

    private String isImmutable;
}
