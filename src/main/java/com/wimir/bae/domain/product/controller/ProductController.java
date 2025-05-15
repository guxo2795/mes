package com.wimir.bae.domain.product.controller;


import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubSearchDTO;
import com.wimir.bae.domain.product.dto.*;
import com.wimir.bae.domain.product.service.ProductService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.excel.service.ExcelService;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.jwt.JwtGlobalService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "01. 기준정보관리 > 03. 품목 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("product")
public class ProductController {

    private final JwtGlobalService jwtGlobalService;
    private final ProductService productService;
    private final ExcelService excelService;

    // 품목 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createProduct(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ProductRegDTO regDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productService.createProduct(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 품목 조회
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<ProductInfoDTO>>> getProductList (
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid ProductSearchDTO searchDTO
    ) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<ProductInfoDTO> list = productService.getProductList(searchDTO);

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
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ProductModDTO modDTO
    ) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productService.updateProduct(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 품목 삭제
    // 삭제 기능은 is_deleted를 이용한 '논리 삭제'로 구현
    // DELETE FROM을 사용해서 데이터를 지우면 '물리 삭제'
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteProduct(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> productKeyList) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productService.deleteProduct(userLoginDTO, productKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("list/excel")
    public ResponseEntity<?> downloadProductExcel(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid ProductSearchDTO searchDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        List<ProductInfoDTO> list = productService.getProductList(searchDTO);

        try (
                // SXSSFWorkbook 객체를 직접 생성해서 데이터를 메모리에 올림. 동적 파일
                // 데이터를 Excel 형식으로 직접 생성하고 바이트 배열로 변환
                // ByteArrayOutputStream 은 내부적으로 byte 배열을 사용하여 데이터를 임시저장, 자동으로 버퍼크기 확장 => 메모리 사용량이 큼
                SXSSFWorkbook workbook = excelService.createExcel(userLoginDTO, list);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            workbook.write(outputStream);
            byte[] contents = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(contents);
        } catch (Exception e) {
            throw new CustomRuntimeException("엑셀 양식 다운로드에 실패했습니다.");
        }
    }

    @PostMapping("/template")
    public ResponseEntity<?> downloadProductTemplate(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {

        jwtGlobalService.getTokenInfo(accessToken, "A");

        try (
                // 이미 만들어진 정적 파일을 읽어와서 반환
                // 파일 크기만큼의 메모리 사용 + 단순 읽기
                InputStream inputStream = new ClassPathResource("productUploadTemplate.xlsx").getInputStream()) {
            byte[] contents = inputStream.readAllBytes();

            String fileName = "품목 업로드 양식.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(contents);
        } catch (Exception e) {
            throw new CustomRuntimeException("엑셀 양식 다운로드에 실패했습니다.");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseDTO<?>> uploadProductTemplate(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @RequestPart(value = "file") MultipartFile file) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        productService.uploadProductTemplate(userLoginDTO, file);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
