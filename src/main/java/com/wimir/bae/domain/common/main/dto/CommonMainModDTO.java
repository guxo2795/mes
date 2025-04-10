package com.wimir.bae.domain.common.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonMainModDTO {

    @NotBlank
    private String mainCommonKey;

    @NotBlank
    @Size(max = 50)
    private String mainCommonName;
}
