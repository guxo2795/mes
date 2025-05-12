package com.wimir.bae.domain.common.sub.controller;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainSearchDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubModDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubSearchDTO;
import com.wimir.bae.domain.common.sub.service.CommonSubService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.dto.ListWrapperDTO;
import com.wimir.bae.global.dto.ResponseDTO;
import com.wimir.bae.global.excel.service.ExcelService;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.jwt.JwtGlobalService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Tag(name = "01. 기준정보관리 > 02. 공통코드 > 02. 하위공통코드")
@RequiredArgsConstructor
@RestController
@RequestMapping("common/sub")
public class CommonSubController {

    private final JwtGlobalService jwtGlobalService;
    private final CommonSubService commonSubService;
    private final ExcelService excelService;


    // 하위 공통 코드 등록
    @PostMapping("create")
    public ResponseEntity<ResponseDTO<?>> createCommonSub(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonSubRegDTO regDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonSubService.createCommonSub(userLoginDTO, regDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return  ResponseEntity.ok().body(responseDTO);
    }

    // 하위 공통 코드 목록
    @GetMapping("list")
    public ResponseEntity<ResponseDTO<List<CommonSubInfoDTO>>> getCommonSubList(
            @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid CommonSubSearchDTO searchDTO) {
        jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonSubInfoDTO> list = commonSubService.getCommonSubList(searchDTO);

        ResponseDTO<List<CommonSubInfoDTO>> responseDTO =
                ResponseDTO.<List<CommonSubInfoDTO>>builder()
                        .result(1)
                        .data(list)
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 하위 공통 코드 수정
    @PostMapping("update")
    public ResponseEntity<ResponseDTO<?>> updateCommonSub(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid CommonSubModDTO modDTO) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonSubService.updateCommonSub(userLoginDTO, modDTO);

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    // 하위 공통 코드 삭제
    @PostMapping("delete")
    public ResponseEntity<ResponseDTO<?>> deleteCommonSub(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ListWrapperDTO<String> subCommonKeyList) {
        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        commonSubService.deleteCommonSub(userLoginDTO, subCommonKeyList.getList());

        ResponseDTO<?> responseDTO =
                ResponseDTO.builder()
                        .result(1)
                        .message("정상 처리 되었습니다.")
                        .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("list/excel")
    public ResponseEntity<?> downloadCommonSubExcel(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessToken,
            @ModelAttribute @Valid CommonSubSearchDTO searchDTO) {

        UserLoginDTO userLoginDTO = jwtGlobalService.getTokenInfo(accessToken, "A");
        List<CommonSubInfoDTO> list = commonSubService.getCommonSubList(searchDTO);

        try (
                SXSSFWorkbook workbook = excelService.createExcel(userLoginDTO, list);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            workbook.write(outputStream);
            byte[] contents = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CommonSubList.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok().headers(headers).body(contents);
        } catch (Exception e) {
            throw new CustomRuntimeException("엑셀 양식 다운로드에 실패했습니다.");
        }
    }

}
