package com.wimir.bae.domain.plan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanMapper {

    //생산 계획 등록
    void createPlan(@Param("productKey") String productKey,
                    @Param("contractCode") String contractCode,
                    @Param("planQuantity") String quantity,
                    @Param("subPlanQuantity") String subPlanQuantity,
                    @Param("planDate") String planDate);
}
