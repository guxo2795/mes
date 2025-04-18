package com.wimir.bae.domain.planTotal.mapper;

import com.wimir.bae.domain.planTotal.dto.PlanTotalInfoDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalModDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    // 실젹 결과 리스트
    List<PlanTotalInfoDTO> getResultList();
    
    // 실적 결과 특이사항 메모
    void updateResultNote(PlanTotalModDTO modDTO);
}
