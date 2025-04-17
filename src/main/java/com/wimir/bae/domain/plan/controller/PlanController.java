package com.wimir.bae.domain.plan.controller;

import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanModDTO;
import com.wimir.bae.domain.plan.service.PlanService;
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
    
    // 생산 계획 수정(팀 수정)
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updatePlan(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid PlanModDTO modDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        planService.updatePlan(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 생산 계획 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deletePlan(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> planKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        planService.deletePlan(userLoginDTO, planKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 생산 계획 확정
    @PostMapping("completed")
    public ResponseEntity<ResponseDTO<?>> executedPlan(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> planKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        planService.executedPlan(userLoginDTO, planKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
