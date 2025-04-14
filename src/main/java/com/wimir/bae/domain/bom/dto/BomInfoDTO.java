package com.wimir.bae.domain.bom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BomInfoDTO {

    private String bomKey;

    private String productCode;

    private String productName;

    private String quantity;

    private String unitName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String finishedKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String materialKey;

    private String rootKey;
}
