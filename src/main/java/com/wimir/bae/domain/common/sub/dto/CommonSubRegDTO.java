package com.wimir.bae.domain.common.sub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonSubRegDTO {

    @NotBlank
    private String mainCommonKey;

    @NotBlank
    private String midCommonKey;

    @NotBlank(message = "{common.subCommonName.NotBlank}")
    @Size(max = 50, message = "{common.subCommonName.Size}")
    private String subCommonName;

    @Size(max = 100, message = "{common.note.Size}")
    private String note;

}
