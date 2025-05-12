package com.wimir.bae.domain.common.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CommonMainSearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    @Schema(example = "페이지 번호")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    @Schema(example = "페이지당 요소 개수")
    private String record;

    @Schema(example = "테이블 정렬")
    private String sort;

    @Schema(hidden = true)
    public String offset;

    @Schema(example = "상위 콩동 코드를 검색")
    private String mainCommonName;
}
