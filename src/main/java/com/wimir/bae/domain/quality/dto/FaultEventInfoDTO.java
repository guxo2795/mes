package com.wimir.bae.domain.quality.dto;

import lombok.Data;

import java.util.List;

@Data
public class FaultEventInfoDTO {
    
    private String faultDate;
    
    private String processName;
    
    private String productCode;
    
    private String productName;
    
    private String faultCount;
    
    // 불량 증상
    private List<FaultEventElementDTO> eventList;

    private String faultStateKey;

    private String productKey;
}
