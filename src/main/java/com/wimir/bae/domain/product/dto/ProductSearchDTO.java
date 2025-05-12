package com.wimir.bae.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ProductSearchDTO {

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    @Schema(example = "페이지 번호")
    private String page;

    @Pattern(regexp = "^(?:[1-9]\\d*|)$", message = "{number.int.OnlyPositive}")
    @Schema(example = "페이지 당 요소 개수")
    private String record;

    @Schema(hidden = true)
    public String offset;

    @Schema(example = "테이블 정렬(-score,name:score 내림차순, name 오름차순)")
    private String sort;

    @Schema(example = "품번 검색")
    private String productCode;

    @Schema(example = "품명 검색")
    private String productName;

    @Schema(example = "업체 키를 검색합니다.")
    private String companyKey;

    @Schema(example = "기종 키를 검색합니다.")
    private String productTypeKey;

    @Schema(example = "공정 유형 키를 검색합니다.")
    private String processTypeKey;

    @Schema(example = "업체 종류 : O - 조달, S - 납품")
    private String companyTypeFlag;

    @Pattern(regexp = "^(F|M|)$", message = "{product.assetTypeFlag.Pattern}")
    @Schema(example = "품목 구분을 검색합니다. : F - 완제품, M - 자재")
    private String assetTypeFlag;
}
