package com.wimir.bae.domain.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderItemSearchDTO {

    @NotBlank
    private String orderKey;
}
