package com.wimir.bae.domain.quality.controller;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import com.wimir.bae.domain.quality.dto.FaultStateModDTO;
import com.wimir.bae.domain.quality.service.FaultStateService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    // 불량 현황 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateFaultState(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid FaultStateModDTO modDTO ) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "U");
        faultStateService.updateFaultState(modDTO,userLoginDTO);

        ResponseDTO<Object> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
