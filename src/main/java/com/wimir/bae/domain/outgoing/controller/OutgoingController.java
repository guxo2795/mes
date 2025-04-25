package com.wimir.bae.domain.outgoing.controller;

import com.wimir.bae.domain.outgoing.dto.*;
import com.wimir.bae.domain.outgoing.service.OutgoingService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("outgoing")
public class OutgoingController {

    private final JwtGlobalService jwtGlobalService;
    private final OutgoingService outgoingService;

    // 출하 등록
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

    // 출고/출하 목록
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<OutgoingShipmentInfoDTO>>> getOutgoingList (
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<OutgoingShipmentInfoDTO> list = outgoingService.getShipmentList();

        ResponseDTO<List<OutgoingShipmentInfoDTO>> response =
                ResponseDTO.<List<OutgoingShipmentInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    // 출하 수정
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO<?>> updateOutgoing (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OutgoingShipmentUpdateDTO outgoingShipmentUpdateDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outgoingService.setOutgoingUpdate(userLoginDTO, outgoingShipmentUpdateDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 출하 삭제
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO<?>> deleteOutgoing (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> outgoingKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outgoingService.deleteOutgoing(userLoginDTO, outgoingKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 출하 확인
    @PostMapping("/complete")
    public ResponseEntity<ResponseDTO<?>> completeOutgoing (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<OutgoingCompleteRegDTO> outgoingKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        outgoingService.outgoingComplete(userLoginDTO, outgoingKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 출하 현황
    @GetMapping("/list/end")
    public ResponseEntity<ResponseDTO<List<OutgoingShipmentEndInfoDTO>>> getOutgoingEndList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<OutgoingShipmentEndInfoDTO> list = outgoingService.getShipmentEndList();

        ResponseDTO<List<OutgoingShipmentEndInfoDTO>> response =
                ResponseDTO.<List<OutgoingShipmentEndInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }
}
