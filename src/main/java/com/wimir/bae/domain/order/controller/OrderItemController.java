package com.wimir.bae.domain.order.controller;

import com.wimir.bae.domain.order.dto.OrderItemRegDTO;
import com.wimir.bae.domain.order.service.OrderItemService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("order/item")
public class OrderItemController {

    private final JwtGlobalService jwtGlobalService;
    private final OrderItemService orderItemService;

    // 자재 발주 품목 등록, 수정, 삭제 동시 진행
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<?>> createOrderItem(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid OrderItemRegDTO orderItemRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        orderItemService.createOrderItem(userLoginDTO, orderItemRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
