package com.wimir.bae.domain.incoming.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IncomingMaterialSearchDTO {

    @NotBlank
    private String orderKey;

}
