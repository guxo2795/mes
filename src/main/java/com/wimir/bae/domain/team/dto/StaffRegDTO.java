package com.wimir.bae.domain.team.dto;

import lombok.Data;

@Data
public class StaffRegDTO {
    
    private String teamKey;

    private String userKey;
    
    // 휴무 여부
    private String isVacated;
}
