package com.wimir.bae.domain.planDetail.controller;

import com.wimir.bae.domain.planDetail.dto.PlanDetailRegDTO;
import com.wimir.bae.domain.planDetail.service.PlanDetailService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("plan/detail")
public class PlanDetailController {

    private final JwtGlobalService jwtGlobalService;
    private final PlanDetailService planDetailService;

    // 작업지시서 생성
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createResultQuantity(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid PlanDetailRegDTO regDTO ) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "U");
        planDetailService.createPlanDetailQuantity(regDTO,userLoginDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
