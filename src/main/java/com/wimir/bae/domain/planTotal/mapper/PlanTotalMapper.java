package com.wimir.bae.domain.planTotal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanTotalMapper {
    
    // 실적 추가
    void createResult(@Param("planKey") String planKey,
                      @Param("teamKey") String teamKey,
                      @Param("productKey") String productKey,
                      @Param("planQuantity") String planQuantity,
                      @Param("executeQuantity") String executeQuantity,
                      @Param("faultQuantity") String faultQuantity,
                      @Param("contractCompleteDate") String contractCompleteDate);

    // 완제품인지 자재인지 확인
    boolean checkAssetTypeFlag(String resultKey);
}
