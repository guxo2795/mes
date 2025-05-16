package com.wimir.bae.domain.common.mid.controller;

import com.wimir.bae.domain.common.mid.dto.CommonMidInfoDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidModDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidRegDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidSearchDTO;
import com.wimir.bae.domain.common.mid.service.CommonMidService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "01. 기준정보관리 > 02. 공통코드 > 02. 중위공통코드")
@RestController
@RequestMapping("common/mid")
@RequiredArgsConstructor
public class CommonMidController {

    private final JwtGlobalService jwtGlobalService;
    private final CommonMidService commonMidService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCommonMid(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMidRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMidService.createCommonMid(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<CommonMidInfoDTO>>> getCommonMidList(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid CommonMidSearchDTO searchDTO) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonMidInfoDTO> list = commonMidService.getCommonMidList(searchDTO);

        ResponseDTO<List<CommonMidInfoDTO>> responseDTO =
                ResponseDTO.<List<CommonMidInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateCommonMid(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMidModDTO modDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMidService.updateCommonMid(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 하위 공통 코드 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteCommonMid(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> midCommonKeyList) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMidService.deleteCommonMid(userLoginDTO, midCommonKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
