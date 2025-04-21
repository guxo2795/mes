package com.wimir.bae.domain.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class OrderModDTO {

    @NotBlank
    private String orderKey;

    @NotBlank
    private String companyKey;

    @NotBlank
    @Size(max = 50)
    private String orderName;

    @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String orderDate;

    @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String sendDate;

}
