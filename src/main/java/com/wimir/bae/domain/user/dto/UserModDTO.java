package com.wimir.bae.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModDTO {

    @NotBlank
    private String userKey;

    @NotBlank
    private String userCode;

    @NotBlank
    private String userName;

    @NotBlank
    private String departmentKey;

    @NotBlank
    private String positionKey;

    @NotBlank
    private String permissionTypeFlag;

    private String phoneNumber;
}