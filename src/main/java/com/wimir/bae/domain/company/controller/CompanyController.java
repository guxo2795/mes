package com.wimir.bae.domain.company.controller;

import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyModDTO;
import com.wimir.bae.domain.company.dto.CompanyProductsInfoDTO;
import com.wimir.bae.domain.company.dto.CompanyRegDTO;
import com.wimir.bae.domain.company.service.CompanyService;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
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
@RequestMapping("company")
public class CompanyController {

    private final JwtGlobalService jwtGlobalService;
    private final CompanyService companyService;
    
    // 업체 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCompany(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CompanyRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        companyService.createCompany(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 업체 목록
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<CompanyInfoDTO>>> getCompanyList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken,"A");
        List<CompanyInfoDTO> list = companyService.getCompanyList();

        ResponseDTO<List<CompanyInfoDTO>> response =
                ResponseDTO.<List<CompanyInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }

    // 업체 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateCompany(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CompanyModDTO modDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        companyService.updateCompany(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
    
    // 업체 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteCompany(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> companyKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        companyService.deleteCompany(userLoginDTO, companyKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 업체와 연결된 모든 품목 목록
    @GetMapping("list/products")
    public ResponseEntity<ResponseDTO<List<CompanyProductsInfoDTO>>> getCompanyProductsList(
            @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CompanyProductsInfoDTO> list = companyService.getCompanyProductsList();

        ResponseDTO<List<CompanyProductsInfoDTO>> response =
                ResponseDTO.<List<CompanyProductsInfoDTO>> builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(response);
    }
}
