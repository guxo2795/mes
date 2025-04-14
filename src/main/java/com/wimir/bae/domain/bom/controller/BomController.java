package com.wimir.bae.domain.bom.controller;

import com.wimir.bae.domain.bom.dto.BomInfoDTO;
import com.wimir.bae.domain.bom.dto.BomRegDTO;
import com.wimir.bae.domain.bom.service.BomService;
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
@RequestMapping("product/bom")
public class BomController {

    private final JwtGlobalService jwtGlobalService;
    private final BomService bomService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createBom(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid BomRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        bomService.createBom(userLoginDTO, regDTO);

        ResponseDTO<?> response =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<BomInfoDTO>>> getBomList (
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<BomInfoDTO> list = bomService.getBomList();

        ResponseDTO<List<BomInfoDTO>> response =
                ResponseDTO.<List<BomInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }
}
