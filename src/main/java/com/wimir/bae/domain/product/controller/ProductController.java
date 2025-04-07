package com.wimir.bae.domain.product.controller;


import com.wimir.bae.domain.product.dto.*;
import com.wimir.bae.domain.product.service.ProductService;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    // 품목 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createProduct(
            @RequestBody @Valid ProductRegDTO regDTO) {

        productService.createProduct(regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 품목 조회
    // 페이징 처리x
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<ProductInfoDTO>>> getProductList () {

        List<ProductInfoDTO> list = productService.getProductList();

        ResponseDTO<List<ProductInfoDTO>> responseDTO =
                ResponseDTO.<List<ProductInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 품목 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateProduct(
            @RequestBody @Valid ProductModDTO modDTO
    ) {

        productService.updateProduct(modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 품목 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteProduct(
        @RequestBody @Valid ListWrapperDTO<String> productKeyList) {

        productService.deleteProduct(productKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
