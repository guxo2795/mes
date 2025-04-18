package com.wimir.bae.domain.planDetail.mapper;

import com.wimir.bae.domain.planDetail.dto.PlanDetailRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanDetailMapper {

    // 작업지시서 등록
    void createPlanDetailQuantity(PlanDetailRegDTO regDTO);



    // 실적 중복 확인
    boolean isExistDetail(@Param("planKey") String planKey,
                          @Param("productKey") String productKey,
                          @Param("executeDate") String executeDate);

    // 작업지시서에 등록된 총 수량
    String getTotalRegisteredQuantity(@Param("planKey") String planKey,
                                      @Param("productKey") String productKey);

    // 생산계획서에 등록된 수주 수량
    String getPlanOrderedQuantity(String planKey, String productKey);
}
