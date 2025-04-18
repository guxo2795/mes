package com.wimir.bae.domain.planDetail.dto;

import lombok.Data;

@Data
public class PlanDetailRegDTO {
    
    private String planKey;
    
    private String teamKey;
    
    private String productKey;

    // 실행 총 수량
    private String executeQuantity;

    // 생산 된 수량
    private String subExecuteQuantity;

    // 불량 수량
    private String faultQuantity;

    // 실행일자 년-월-일
    private String executeDate;
    
    // 행(셀) 색상
    private String rowColor;
}
