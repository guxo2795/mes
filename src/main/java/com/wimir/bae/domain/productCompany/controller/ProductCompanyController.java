package com.wimir.bae.domain.productCompany.controller;

import com.wimir.bae.domain.productCompany.dto.ProductCompanyInfoDTO;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyModDTO;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import com.wimir.bae.domain.productCompany.service.ProductCompanyService;
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
@RequestMapping("productCompany")
public class ProductCompanyController {

    private final JwtGlobalService jwtGlobalService;
    private final ProductCompanyService productCompanyService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<?>> createProductCompany(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ProductCompanyRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productCompanyService.createProductCompany(userLoginDTO, regDTO);

        ResponseDTO<?> response =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<ProductCompanyInfoDTO>>> getProductCompanyList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<ProductCompanyInfoDTO> list = productCompanyService.getProductCompanyList();

        ResponseDTO<List<ProductCompanyInfoDTO>> response =
                ResponseDTO.<List<ProductCompanyInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDTO<?>> updateProductCompany(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ProductCompanyModDTO modDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productCompanyService.updateProductCompany(userLoginDTO, modDTO);

        ResponseDTO<?> response =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(response);
    }
}
