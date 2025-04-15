package com.wimir.bae.domain.contract.controller;


import com.wimir.bae.domain.contract.dto.*;
import com.wimir.bae.domain.contract.service.ContractService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("contract")
public class ContractController {

    private final ContractService contractService;
    private final JwtGlobalService jwtGlobalService;

    // 수주 등록
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

    // 수주 목록
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<ContractInfoDTO>>> getContractList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<ContractInfoDTO> list = contractService.getContractList();

        ResponseDTO<List<ContractInfoDTO>> response =
                ResponseDTO.<List<ContractInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }
    
    // 수주 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateContract(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ContractModDTO modDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        contractService.updateContract(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    

    // 수주 전 자재를 가져오는 api
    @GetMapping("material/start/list")
    public ResponseEntity<ResponseDTO<List<ContractMaterialInfoDTO>>> listContractMaterial(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid ContractMaterialSearchDTO searchDTO ) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<ContractMaterialInfoDTO> list = contractService.listContractMaterial(searchDTO);

        ResponseDTO<List<ContractMaterialInfoDTO>> responseDTO =
                ResponseDTO.<List<ContractMaterialInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
