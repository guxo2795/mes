package com.wimir.bae.domain.outgoing.controller;

import com.wimir.bae.domain.outgoing.dto.OutgoingShipmentRegDTO;
import com.wimir.bae.domain.outgoing.service.OutgoingService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("outgoing")
public class OutgoingController {

    private final JwtGlobalService jwtGlobalService;
    private final OutgoingService outgoingService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createOutgoing(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutgoingShipmentRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outgoingService.createOutgoing(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
