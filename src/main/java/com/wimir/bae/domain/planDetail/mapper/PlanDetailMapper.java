package com.wimir.bae.domain.planDetail.mapper;

import com.wimir.bae.domain.planDetail.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanDetailMapper {

    // 작업지시서 등록
    void createPlanDetailQuantity(PlanDetailRegDTO regDTO);

    // 작업지시서 상세 리스트
    List<DetailInfoDTO> getDetailList(DetailSearchDTO searchDTO);

    // 작업지시서 상세 수정 (계획, 생산, 불량 수량)
    void updatePlanDetailQuantity(PlanDetailQuantityModDTO regDTO);

    // 작업지시서 상세 삭제
    void deletePlanDetail(String detailKey);

    // 작업지시서 상세 확정
    void updatePlanDetailCompleted(String detailKey);


    // 실적 중복 확인
    boolean isExistDetail(@Param("planKey") String planKey,
                          @Param("productKey") String productKey,
                          @Param("executeDate") String executeDate);

    // 작업지시서에 등록된 총 수량
    String getTotalRegisteredQuantity(@Param("planKey") String planKey,
                                      @Param("productKey") String productKey);

    // 생산계획서에 등록된 수주 수량
    String getPlanOrderedQuantity(@Param("planKey") String planKey,
                                  @Param("productKey") String productKey);

    // 지시서 확정 여부
    boolean checkPlanDetailCompleted(String detailKey);

    // 작업지시서 상세+생산실적 정보
    List<DetailWarehouseDTO> listDetailWarehouse(String detailKey);

    // 불량 결과 쪽 값 전달 업데이트 (추가 + 기능)
    void updateFaultForTotal(@Param("planKey") String planKey,
                             @Param("productKey") String productKey,
                             @Param("faultQuantity") int faultQuantity);
    // 생산 결과 쪽 값 전달 업데이트 (추가 + 기능)
    void updateExecuteForTotal(@Param("planKey") String planKey,
                               @Param("productKey") String productKey,
                               @Param("executeQuantity") int executeQuantity);

    // 생산 결과 쪽 값 전달 업데이트 (추가 + 기능)
    void updateExecuteForPlan(String planKey, String productKey, int executeQuantity);
}
