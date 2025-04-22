package com.wimir.bae.domain.order.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class OrderItemRegDTO {

    @NotBlank
    private String orderKey;

    @Valid
    private List<OrderItemRegDetailDTO> list;

}
