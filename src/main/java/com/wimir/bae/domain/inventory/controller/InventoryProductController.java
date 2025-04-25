package com.wimir.bae.domain.inventory.controller;

import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.dto.InventoryProductInfoDTO;
import com.wimir.bae.domain.inventory.dto.InventoryProductInoutMoveDTO;
import com.wimir.bae.domain.inventory.service.InventoryProductService;
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
@RequestMapping("inventory/product")
public class InventoryProductController {

    private final JwtGlobalService jwtGlobalService;
    private final InventoryProductService inventoryProductService;

    // 재고 증가
    @PostMapping("/increase")
    public ResponseEntity<ResponseDTO<?>> increaseProductInventory(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid InventoryCorrectionDTO inventoryCorrectionDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        inventoryProductService.increaseProductInventory(userLoginDTO, inventoryCorrectionDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 재고 감소
    @PostMapping("/decrease")
    public ResponseEntity<ResponseDTO<?>> decreaseProductInventory(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid InventoryCorrectionDTO inventoryCorrectionDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        inventoryProductService.decreaseProductInventory(userLoginDTO, inventoryCorrectionDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 재고 현황
    @GetMapping("/status")
    public ResponseEntity<ResponseDTO<List<InventoryProductInfoDTO>>> getProductInventoryStatus(
            @RequestHeader("Authorization") String accessToken) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<InventoryProductInfoDTO> list = inventoryProductService.getProductInventoryStatus();

        ResponseDTO<List<InventoryProductInfoDTO>> response =
                ResponseDTO.<List<InventoryProductInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    // 재고 이동
    @PostMapping("/status/move")
    public ResponseEntity<ResponseDTO<?>> moveProductWarehouse(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid InventoryProductInoutMoveDTO inventoryProductInoutMoveDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        inventoryProductService.moveProductWarehouse(userLoginDTO, inventoryProductInoutMoveDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

}
