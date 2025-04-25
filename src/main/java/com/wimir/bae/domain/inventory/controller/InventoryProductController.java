package com.wimir.bae.domain.inventory.controller;

import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.service.InventoryProductService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("inventory/product")
public class InventoryProductController {

    private final JwtGlobalService jwtGlobalService;
    private final InventoryProductService inventoryProductService;

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


}
