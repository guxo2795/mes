package com.wimir.bae.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdatePasswordDTO {

    @NotBlank(message = "{user.userCode.NotBlank}")
    private String userCode;

    @NotBlank(message = "{user.password.NotBlank}")
    private String password;

}
