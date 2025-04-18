package com.wimir.bae.domain.planDetail.dto;

import lombok.Data;

@Data
public class DetailInfoDTO {

    private String detailKey;

    private String executeDate;

    private String executeQuantity;

    private String subExecuteQuantity;

    private String faultQuantity;

    private String isCompleted;

}
