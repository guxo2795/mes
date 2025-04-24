package com.wimir.bae.domain.outgoing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OutgoingShipmentUpdateDTO {

    @NotBlank
    private String outgoingKey;

    @NotBlank
    private String warehouseKey;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)?$")
    private String outgoingDateTime;
}
