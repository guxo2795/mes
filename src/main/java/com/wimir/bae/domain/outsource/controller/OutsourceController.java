package com.wimir.bae.domain.outsource.controller;

import com.wimir.bae.domain.outsource.dto.*;
import com.wimir.bae.domain.outsource.service.OutsourceService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    // 외주 수정
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO<?>> updateOutsource (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutsourceUpdateDTO outsourceUpdateDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outsourceService.updateOutsource(userLoginDTO, outsourceUpdateDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);

    }

    // 외주 삭제
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO<?>> updateOutsource (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> outsourceKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outsourceService.deleteOutsource(userLoginDTO, outsourceKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 외주 등록 가능한 품목 조회
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<OutsourceContractListDTO>>> getOutsourceList (
             @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<OutsourceContractListDTO> list = outsourceService.getOutsourceContractList();

        ResponseDTO<List<OutsourceContractListDTO>> response =
                ResponseDTO.<List<OutsourceContractListDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    // 입하/입고 예정 목록 조회
    @GetMapping("/incoming/list")
    public ResponseEntity<ResponseDTO<List<OutsourceIncomingListDTO>>> getOutsourceIncomingList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<OutsourceIncomingListDTO> list = outsourceService.getOutsourceIncomingList();

        ResponseDTO<List<OutsourceIncomingListDTO>> response =
                ResponseDTO.<List<OutsourceIncomingListDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);

    }

    // 외주 입하 완료
    @PostMapping("/inbound/complete")
    public ResponseEntity<ResponseDTO<?>> completeOutsourceInbound(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutsourceInboundCompleteRegDTO outsourceInboundCompleteRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outsourceService.setOutsourceInbound(userLoginDTO, outsourceInboundCompleteRegDTO.getOutsourceKey());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/incoming/complete")
    public ResponseEntity<ResponseDTO<?>> completeOutsourceInbound(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutsourceIncomingCompleteRegDTO outsourceIncomingCompleteRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outsourceService.setOutsourceIncoming(userLoginDTO, outsourceIncomingCompleteRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}