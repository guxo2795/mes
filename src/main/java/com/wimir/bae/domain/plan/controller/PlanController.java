package com.wimir.bae.domain.plan.controller;

import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.service.PlanService;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("plan")
public class PlanController {

    private final JwtGlobalService jwtGlobalService;
    private final PlanService planService;


    // 생산 계획 등록은 수주실행 시 등록됨.

    // 생산 계획 리스트 조회
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<PlanInfoDTO>>> getPlanList(
        @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken,"A");
        List<PlanInfoDTO> planInfoDTOList = planService.getPlanList();

        ResponseDTO<List<PlanInfoDTO>> responseDTO =
                ResponseDTO.<List<PlanInfoDTO>> builder()
                        .result(1)
                        .data(planInfoDTOList)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
