package com.wimir.bae.domain.quality.controller;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import com.wimir.bae.domain.quality.service.FaultStateService;
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
@RequestMapping("fault/state")
public class FaultStateController {

    private final JwtGlobalService jwtGlobalService;
    private final FaultStateService faultStateService;

    // 불량 현황 조회
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<FaultStateDTO>>> getFaultStateList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<FaultStateDTO> list = faultStateService.getFaultStateList();

        ResponseDTO<List<FaultStateDTO>> responseDTO =
                ResponseDTO.<List<FaultStateDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
