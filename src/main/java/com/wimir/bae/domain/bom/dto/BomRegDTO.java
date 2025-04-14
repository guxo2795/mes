package com.wimir.bae.domain.bom.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class BomRegDTO {

    @NotBlank
    private String rootKey; // 최상위 품목 키(완제품 키)

    @NotBlank
    private String finishedKey; // 부모 키(처음은 완제품)

    @NotBlank
    private String materialKey; // 자재 키(하위 키)

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$") // 양의 정수, 소수
    private String quantity;
}
