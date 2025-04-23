package com.wimir.bae.domain.outsource.controller;

import com.wimir.bae.domain.outsource.dto.OutsourceRegDTO;
import com.wimir.bae.domain.outsource.service.OutsourceService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("outsource")
public class OutsourceController {

    private final JwtGlobalService jwtGlobalService;
    private final OutsourceService outsourceService;

    // 외주 등록
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<?>> createOutsource(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutsourceRegDTO outsourceRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outsourceService.createOutsource(userLoginDTO, outsourceRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}