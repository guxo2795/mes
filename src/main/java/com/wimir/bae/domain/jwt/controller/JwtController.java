package com.wimir.bae.domain.jwt.controller;


import com.wimir.bae.domain.jwt.dto.JwtLoginDTO;
import com.wimir.bae.domain.jwt.service.JwtService;
import com.wimir.bae.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("jwt")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<ResponseDTO<String>> login(
            @RequestBody @Valid JwtLoginDTO jwtLoginDTO,
            HttpServletResponse response
    ) {
        String refreshToken = jwtService.login(jwtLoginDTO);

        // refreshToken을 쿠키에 담아서 클라이언트에게 보내줌
        ResponseCookie cookie =
                ResponseCookie.from("Bae", refreshToken)
                        .maxAge(7 * 24 * 60 * 60) // 쿠키 만료 기간: 7일
                        .path("/") // 사이트 전체에서 사용 가능
                        .httpOnly(true) // 자바스크립트로 쿠키 읽을 수 없음(XSS 공격 방지), 서버만 읽을 수 있음
                        .sameSite("Lax") // local https, 개발중일때는 Lax 기본값으로 사용
                        .secure(false) // local https, 개발중일때는 false로 사용
                        .build();
        response.addHeader("Set-Cookie", cookie.toString()); // 쿠키를 HTTP 응답 헤더에 포함시켜서 클라이언트에게 전송

        // accessToken 발급
        String accessToken = jwtService.silent(refreshToken);

        // 응답 바디
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .message("로그인 되었습니다.")
                        .data(accessToken)
                        .result(1)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
