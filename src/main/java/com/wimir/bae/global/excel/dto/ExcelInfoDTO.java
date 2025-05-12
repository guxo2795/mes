package com.wimir.bae.global.excel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExcelInfoDTO {

    @Schema(example = "컬럼 영문명")
    private String engColumn;
    
    @Schema(example = "컬럼 한글명")
    private String korColumn;
    
    @Schema(example = "엑셀 셀 표시 형식")
    private String isNumber;
    
    @Schema(example = "셀 값 변환 여부")
    private String isConverted;
}
