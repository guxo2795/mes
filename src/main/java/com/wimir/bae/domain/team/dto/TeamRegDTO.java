package com.wimir.bae.domain.team.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TeamRegDTO {

    @NotBlank
    private String teamName;

    private String subCommonKey;
}
