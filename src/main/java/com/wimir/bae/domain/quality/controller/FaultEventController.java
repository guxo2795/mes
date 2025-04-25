package com.wimir.bae.domain.quality.controller;

import com.wimir.bae.domain.quality.dto.FaultEventInfoDTO;
import com.wimir.bae.domain.quality.service.FaultEventService;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("fault/event")
public class FaultEventController {

    private final JwtGlobalService jwtGlobalService;
    private final FaultEventService faultEventService;

    // 불량 내역 조회
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<FaultEventInfoDTO>>> getFaultEventList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<FaultEventInfoDTO> list = faultEventService.getFaultEventList();

        ResponseDTO<List<FaultEventInfoDTO>> responseDTO =
                ResponseDTO.<List<FaultEventInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
