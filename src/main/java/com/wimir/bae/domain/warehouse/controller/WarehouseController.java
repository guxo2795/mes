package com.wimir.bae.domain.warehouse.controller;

import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import com.wimir.bae.domain.warehouse.service.WarehouseService;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    
    
    // 창고 수정
    
    
    // 창고 삭제

}
