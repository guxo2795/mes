package com.wimir.bae.domain.contract.controller;


import com.wimir.bae.domain.contract.dto.*;
import com.wimir.bae.domain.contract.service.ContractService;
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

    // 수주 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteContract(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> contractKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        contractService.deletedContract(userLoginDTO, contractKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 수주 실행
    @PostMapping("start")
    public ResponseEntity<ResponseDTO<?>> startContract(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid StartContractRequestDTO requestDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        // 단일 계약 키를 전달
        contractService.startContract(userLoginDTO, requestDTO.getContractCode(), requestDTO.getPlanDate(), requestDTO.getWarehouseKeyMap());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 수주 실행 전 품목정보를 가져오는 api
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
