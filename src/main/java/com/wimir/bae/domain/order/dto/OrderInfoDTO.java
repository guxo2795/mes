package com.wimir.bae.domain.order.dto;

import lombok.Data;

@Data
public class OrderInfoDTO {

    private String orderKey;

    private String orderName;

    private String isCompleted;

    private String companyKey;

    private String companyName;

    private String orderDate;

    private String sendDate;

    private int totalOrderPrice;
}
