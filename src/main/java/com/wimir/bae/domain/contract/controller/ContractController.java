package com.wimir.bae.domain.contract.controller;


import com.wimir.bae.domain.contract.dto.ContractRegDTO;
import com.wimir.bae.domain.contract.service.ContractService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("contract")
public class ContractController {

    private final ContractService contractService;
    private final JwtGlobalService jwtGlobalService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createContract(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ContractRegDTO contractRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        contractService.createContract(userLoginDTO, contractRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
