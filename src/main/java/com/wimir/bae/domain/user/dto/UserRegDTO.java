package com.wimir.bae.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegDTO {

    @NotBlank
    @Size(max = 25)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]+$")
    private String userCode;

    @NotBlank
    private String password;

    @NotBlank
    private String userName;

    @NotBlank
    private String departmentKey;

    @NotBlank
    private String positionKey;

    @NotBlank
    @Pattern(regexp = "^[UA]$")
    private String permissionTypeFlag;

    private String phoneNumber;
}
