package com.wimir.bae.domain.common.sub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonSubModDTO {

    private String subCommonKey;

    @Size(max = 50, message = "{common.subCommonName.Size}")
    private String subCommonName;

    @Size(max = 100, message = "{common.note.Size}")
    private String note;
}
