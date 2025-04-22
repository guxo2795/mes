package com.wimir.bae.domain.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OrderItemRegDetailDTO {

    // 발주 품목 키
    // 입력 시 발주 품목 수정,삭제
    private String orderMaterialKey;

    // 자재 키
    @NotBlank
    private String materialKey;

    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*)(\\.\\d+)?$")
    private String orderQuantity;
}
