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

    @NotBlank(message = "{user.userCode.NotBlank}")
    @Size(max = 25, message = "{user.userCode.Size}")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]+$", message = "{user.userCode.Pattern}")
    private String userCode;

    @NotBlank(message = "{user.password.NotBlank}")
    private String password;

    @NotBlank(message = "{user.userName.NotBlank}")
    @Size(max = 50, message = "{user.userName.Size}")
    private String userName;

    @NotBlank(message = "{user.departmentKey.NotBlank}")
    private String departmentKey;

    @NotBlank(message = "{user.positionKey.NotBlank}")
    private String positionKey;

    @NotBlank(message = "{user.permissionTypeFlag.NotBlank}")
    @Pattern(regexp = "^[UA]$", message = "{user.permissionTypeFlag.Pattern}")
    private String permissionTypeFlag;

    private String phoneNumber;

    private String userImageKey;

    private String userSignatureKey;
}
