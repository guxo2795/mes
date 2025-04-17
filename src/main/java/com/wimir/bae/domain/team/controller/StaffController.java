package com.wimir.bae.domain.team.controller;

import com.wimir.bae.domain.team.dto.StaffRegDTO;
import com.wimir.bae.domain.team.service.StaffService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("staff")
public class StaffController {

    private final JwtGlobalService jwtGlobalService;
    private final StaffService staffService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createStaff(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid StaffRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        staffService.createStaff(regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
