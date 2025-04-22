package com.wimir.bae.domain.incoming.controller;

import com.wimir.bae.domain.incoming.dto.IncomingArrivalInfoDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalRegDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalSearchDTO;
import com.wimir.bae.domain.incoming.service.IncomingArrivalService;
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
@RequestMapping("incoming/arrival")
public class IncomingArrivalController {

    private final JwtGlobalService jwtGlobalService;
    private final IncomingArrivalService incomingArrivalService;

    // 자재 입하 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createArrival(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid IncomingArrivalRegDTO incomingArrivalRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        incomingArrivalService.createArrival(userLoginDTO, incomingArrivalRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 자재 입하 목록
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<IncomingArrivalInfoDTO>>> getArrivalList(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid IncomingArrivalSearchDTO incomingArrivalSearchDTO) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<IncomingArrivalInfoDTO> list = incomingArrivalService.getArrivalList(incomingArrivalSearchDTO);

        ResponseDTO<List<IncomingArrivalInfoDTO>> response =
                ResponseDTO.<List<IncomingArrivalInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

}
