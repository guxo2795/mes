package com.wimir.bae.domain.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtLoginDTO {

    @NotBlank(message = "아이디를 입력해 주세요.")
    private String userCode;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}
