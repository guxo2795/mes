package com.wimir.bae.domain.plan.mapper;

import com.wimir.bae.domain.plan.dto.PlanContractInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanInfoDTO;
import com.wimir.bae.domain.plan.dto.PlanModDTO;
import com.wimir.bae.domain.plan.dto.PlanTotalSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface PlanMapper {

    // 생산 계획 등록
    void createPlan(@Param("productKey") String productKey,
                    @Param("contractCode") String contractCode,
                    @Param("planQuantity") String quantity,
                    @Param("subPlanQuantity") String subPlanQuantity,
                    @Param("planDate") String planDate);

    // 생산 계획 리스트 조회
    List<PlanInfoDTO> getPlanList();

    // 생산 계획 수정
    void updatePlan(PlanModDTO modDTO);

    // 생산 계획 삭제
    void deletePlan(String planKey);

    // 생산 계획 및 실적 조회
    PlanInfoDTO getProductionPlan(String contractCode);

    // 생산 실적 테이블 조회
    PlanTotalSearchDTO getProductPlanTotal(@Param("planKey") String planKey,
                                           @Param("productKey") String productKey);
    // 생산 계획 키 유효성
    boolean isPlanKeyExist(String planKey);
    boolean isPlanAlreadyExecuted(String planKey);

    // 실행 중인 수주 리스트
    List<PlanContractInfoDTO> listPlanContract();

    // 실행 중인 수주 리스트를 total 데이터에 넣기 위한 select
    List<PlanContractInfoDTO> getPlanContractInfoList(@Param("planKey") String planKey,
                                                      @Param("outsourced") String outsourced);
    
    // 생산 계획 팀 존재 확인
    boolean isPlanTeamExist(String planKey);

    // 생산 계획 확정
    void executedPlan(String planKey);
}
