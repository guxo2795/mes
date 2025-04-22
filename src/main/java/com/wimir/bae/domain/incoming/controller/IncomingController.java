package com.wimir.bae.domain.incoming.controller;

import com.wimir.bae.domain.incoming.dto.IncomingRegDTO;
import com.wimir.bae.domain.incoming.service.IncomingService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("incoming")
public class IncomingController {

    private final JwtGlobalService jwtGlobalService;
    private final IncomingService incomingService;

    // 자재 입고 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createIncoming(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid IncomingRegDTO incomingRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        incomingService.createIncoming(userLoginDTO, incomingRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok(responseDTO);
    }




    // 자재 입하/입고 현황





}
