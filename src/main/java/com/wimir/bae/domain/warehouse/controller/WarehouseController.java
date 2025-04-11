package com.wimir.bae.domain.warehouse.controller;

import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseInfoDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseModDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import com.wimir.bae.domain.warehouse.service.WarehouseService;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("warehouse")
public class WarehouseController {

    private final JwtGlobalService jwtGlobalService;
    private final WarehouseService warehouseService;
    
    // 창고 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createWarehouse(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid WarehouseRegDTO warehouseRegDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        warehouseService.createWarehouse(userLoginDTO, warehouseRegDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 창고 조회
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<WarehouseInfoDTO>>> getWarehouseList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<WarehouseInfoDTO> list = warehouseService.getWarehouseList();

        ResponseDTO<List<WarehouseInfoDTO>> response =
                ResponseDTO.<List<WarehouseInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }
    
    // 창고 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateWarehouse (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid WarehouseModDTO modDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        warehouseService.updateWarehouse(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 창고 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteWarehouse(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> warehouseKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        warehouseService.deleteWarehouse(userLoginDTO ,warehouseKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
