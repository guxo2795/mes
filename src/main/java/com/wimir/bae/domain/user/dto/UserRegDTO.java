package com.wimir.bae.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "아이디")
    private String userCode;

    @NotBlank(message = "{user.password.NotBlank}")
    @Schema(example = "비밀번호")
    private String password;

    @NotBlank(message = "{user.userName.NotBlank}")
    @Size(max = 50, message = "{user.userName.Size}")
    @Schema(example = "이름")
    private String userName;

    @NotBlank(message = "{user.departmentKey.NotBlank}")
    @Schema(example = "부서 키")
    private String departmentKey;

    @NotBlank(message = "{user.positionKey.NotBlank}")
    @Schema(example = "직위 키")
    private String positionKey;

    @NotBlank(message = "{user.permissionTypeFlag.NotBlank}")
    @Pattern(regexp = "^[UA]$", message = "{user.permissionTypeFlag.Pattern}")
    @Schema(example = "권한")
    private String permissionTypeFlag;

    @Schema(example = "전화번호")
    private String phoneNumber;

    @Schema(hidden = true)
    private String userImageKey;

    @Schema(hidden = true)
    private String userSignatureKey;
}
