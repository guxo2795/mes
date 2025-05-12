package com.wimir.bae.domain.common.main.controller;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainModDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainRegDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainSearchDTO;
import com.wimir.bae.domain.common.main.service.CommonMainService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.excel.service.ExcelService;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.jwt.JwtGlobalService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Tag(name = "01. 기준정보관리 > 02. 공통코드 > 01. 상위공통코드")
@RequiredArgsConstructor
@RestController
@RequestMapping("common/main")
public class CommonMainController {

    private final JwtGlobalService jwtGlobalService;
    private final CommonMainService commonMainService;
    private final ExcelService excelService;

    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMainRegDTO regDTO) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.createCommonMain(regDTO.getMainCommonName());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<CommonMainInfoDTO>>> getCommonMainList(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid CommonMainSearchDTO searchDTO) {

        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonMainInfoDTO> list = commonMainService.getCommonMainList(searchDTO);

        ResponseDTO<List<CommonMainInfoDTO>> responseDTO =
                ResponseDTO.<List<CommonMainInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonMainModDTO modDTO) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.updateCommonMain(modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteCommonMain(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> mainCommonKeyList) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        commonMainService.deleteCommonMain(mainCommonKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("list/excel")
    public ResponseEntity<?> downloadCommonMainExcel(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid CommonMainSearchDTO searchDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonMainInfoDTO> list = commonMainService.getCommonMainList(searchDTO);

        try (
                SXSSFWorkbook workbook = excelService.createExcel(userLoginDTO, list);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ) {
            workbook.write(outputStream);
            byte[] contents = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CommonMainList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(contents);
        } catch (Exception e) {
            throw new CustomRuntimeException("엑셀 양식 다운로드에 실패했습니다.");
        }
    }


}
