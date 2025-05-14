package com.wimir.bae.domain.bom.dto;

import lombok.Data;

@Data
public class BomMaterialDTO {

    private String bomKey;

    private String materialKey;

    private String materialCode;

    private String materialName;

    private String quantity;

    private String unitName;

}
