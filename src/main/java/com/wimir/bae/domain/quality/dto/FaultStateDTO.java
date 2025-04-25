package com.wimir.bae.domain.quality.dto;

import lombok.Data;

@Data
public class FaultStateDTO {

    private String faultStateKey;

    private String processName;

    private String processType;

    private String faultDate;

    private String productCode;

    private String productName;

    private String faultCount;

    private String productKey;

    // 작업지시 상세 키, 외주 키
    private String keyCode;
}
