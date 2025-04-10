package com.wimir.bae.domain.common.sub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonSubRegDTO {

    private String mainCommonKey;

    @Size(max = 50)
    private String subCommonName;

    @Size(max = 100)
    private String note;

}
