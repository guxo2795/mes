package com.wimir.bae.domain.common.main.controller;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainModDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainRegDTO;
import com.wimir.bae.domain.common.main.service.CommonMainService;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("common/main")
public class CommonMainController {

    private final JwtGlobalService jwtGlobalService;
    private final CommonMainService commonMainService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMainRegDTO regDTO) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.createCommonMain(regDTO.getMainCommonName());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<CommonMainInfoDTO>>> getCommonMainList(
            @RequestHeader("Authorization") String accessToken) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonMainInfoDTO> list = commonMainService.getCommonMainList();

        ResponseDTO<List<CommonMainInfoDTO>> responseDTO =
                ResponseDTO.<List<CommonMainInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMainModDTO modDTO) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.updateCommonMain(modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> mainCommonKeyList) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.deleteCommonMain(mainCommonKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
