package com.wimir.bae.domain.planDetail.dto;

import lombok.Data;

@Data
public class DetailWarehouseDTO {

    private String planKey;

    private String productKey;

    private String contractName;

    private String detailKey;

    private String executeQuantity;

    private String faultQuantity;

    private String executeDate;

    private String resultKey;
}
