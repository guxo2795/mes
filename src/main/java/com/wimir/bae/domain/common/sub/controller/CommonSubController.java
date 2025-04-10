package com.wimir.bae.domain.common.sub.controller;

import com.wimir.bae.domain.common.sub.service.CommonSubService;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("common/sub")
public class CommonSubController {

    private final JwtGlobalService jwtGlobalService;
    private final CommonSubService commonSubService;


    // 하위 공통 코드 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCommonSub(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonSubRegDTO regDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonSubService.createCommonSub(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return  ResponseEntity.ok().body(responseDTO);
    }

    // 하위 공통 코드 목록

    // 하위 공통 코드 수정

    // 하위 공통 코드 삭제
}
