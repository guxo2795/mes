package com.wimir.bae.domain.common.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonMainRegDTO {

    @Size(max = 50)
    private String mainCommonName;
}
