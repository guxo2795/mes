package com.wimir.bae.domain.user.controller;

import com.wimir.bae.domain.user.dto.UserRegDTO;
import com.wimir.bae.domain.user.service.UserService;
import com.wimir.bae.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

//    private final JwtGlobalService jwtGlobalService;
    private final UserService userService;

    public ResponseEntity<ResponseDTO<?>> createUser(
            @RequestBody @Valid UserRegDTO regDTO) {

        userService.createUser(regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
