package com.wimir.bae.domain.planTotal.mapper;

import com.wimir.bae.domain.planTotal.dto.PlanTotalInfoDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalModDTO;
import com.wimir.bae.domain.planTotal.dto.PlanTotalResultDTO;
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

    // 실적 결과 확정 여부
    boolean isCompleted(String resultKey);

    // 완제품에 필요한 자재 실적 결과 확정 여부
    boolean isCompletedMaterial(String resultKey);

    // 실적 결과 확정
    void completedResult(String resultKey);

    // 실적 확정에 필요한 리스트
    List<PlanTotalResultDTO> listPlanTotalResult(String resultKey);

    // 여기서 실적 확정하고 그것에 대한 값을 만들어주는 것이다 --------
    // incoming 자재 추가
    void insertIncomingProduct(
            @Param("productKey") String productKey,
            @Param("warehouseKey") String warehouseKey,
            @Param("contractMaterialKey") String contractMaterialKey,
            @Param("executeQuantity") double executeQuantity,
            @Param("incomingTypeFlag") String incomingTypeFlag,
            @Param("note") String note);

    // 창고 업데이트
    void updateWarehouse(String warehouseKey, String resultKey);

    // inventory에 값이 있는지 체크
    boolean checkInventory(String productKey, String warehouseKey);

    // inventory 업로드
    void updateInventory(String productKey, String warehouseKey, double executeQuantity);
}
