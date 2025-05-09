package com.wimir.bae.domain.order.controller;

import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderModDTO;
import com.wimir.bae.domain.order.dto.OrderRegDTO;
import com.wimir.bae.domain.order.service.OrderService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;
    private final JwtGlobalService jwtGlobalService;

    // 자재 발주 등록
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<?>> createOrder(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OrderRegDTO orderRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        orderService.createOrder(userLoginDTO, orderRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 자재 발주 목록
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<OrderInfoDTO>>> getOrderList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<OrderInfoDTO> list = orderService.getOrderList();

        ResponseDTO<List<OrderInfoDTO>> response =
                ResponseDTO.<List<OrderInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    // 자재 발주 수정
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO<?>> updateOrder(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OrderModDTO orderModDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        orderService.updateOrder(userLoginDTO, orderModDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 자재 발주 삭제
    @PostMapping("/delete")
    public ResponseEntity<ResponseDTO<?>> deleteOrder(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> orderKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        orderService.deleteOrder(userLoginDTO, orderKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 자재 발주 완료
    @PostMapping("/complete")
    public ResponseEntity<ResponseDTO<?>> completeOrder(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> orderKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        orderService.completeOrder(userLoginDTO, orderKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
