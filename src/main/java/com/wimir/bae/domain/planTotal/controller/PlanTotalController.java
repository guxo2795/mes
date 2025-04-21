package com.wimir.bae.domain.planTotal.controller;

import com.wimir.bae.domain.plan.dto.PlanTotalSearchDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalCompletedDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalInfoDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalModDTO;
import com.wimir.bae.domain.planTotal.service.PlanTotalService;
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
@RequestMapping("plan/result")
public class PlanTotalController {

    private final JwtGlobalService jwtGlobalService;
    private final PlanTotalService planTotalService;

    // 생산 실적 결과 리스트
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<PlanTotalInfoDTO>>> getResultList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<PlanTotalInfoDTO> list = planTotalService.getResultList();

        ResponseDTO<List<PlanTotalInfoDTO>> responseDTO =
                ResponseDTO.<List<PlanTotalInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 생산 실적 결과 특이사항 메모
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateResultNote(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid PlanTotalModDTO modDTO ) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        planTotalService.updateResultNote(modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 생산 실적 결과 확인
    @PostMapping("completed")
    public ResponseEntity<ResponseDTO<?>> completedResult(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid PlanTotalCompletedDTO planTotalCompletedDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        planTotalService.completedResult(userLoginDTO, planTotalCompletedDTO.getResultKey() , planTotalCompletedDTO.getWarehouseKey());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
