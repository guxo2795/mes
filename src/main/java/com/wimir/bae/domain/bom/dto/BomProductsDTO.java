package com.wimir.bae.domain.bom.dto;

import lombok.Data;

import java.util.List;

@Data
public class BomProductsDTO {

    private String rootKey;

    private String finishedKey;

    private String finishedCode;

    private String finishedName;

    private List<BomMaterialDTO> materials;

}
