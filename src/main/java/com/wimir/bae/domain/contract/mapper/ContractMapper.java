package com.wimir.bae.domain.contract.mapper;

import com.wimir.bae.domain.contract.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface ContractMapper {
    
    // 수주 등록
    void createContract(ContractRegDTO contractRegDTO);

    // 수주 목록 조회
    List<ContractInfoDTO> getContractList();
    // 실행중인 수주 리스트
    List<ContractInfoDTO> getStartContractInfoList();

    // 수주 정보
    ContractInfoDTO getContractInfo(String contractCode);
    ContractInfoDTO getContractAndItemInfo(String contractKey);

    //수주 정보 가지고오기(팀 연결 전 사용)
    ContractInfoDTO getContractForPlan(String contractCode);

    // 수주 실행 시 품목 자재들 등록에 필요한 내용 불러오기
    List<ContractMaterialInfoDTO> listContractMaterial(@Param("contractCode")String contractCode,
                                                       @Param("processTypeKey") String processTypeKey);

    // 수주 수정
    void updateContract(ContractModDTO modDTO);

    // 수주 품목 등록
    void createContractMaterials(ContractRegDTO contractRegDTO);

    // 수주 품목 수정
    void updateContractMaterial(@Param("contractCode") String contractCode,
                                @Param("productKey") String productKey,
                                @Param("quantity") String quantity);

    // 수주 삭제
    void deletedContract(String contractCode);

    // 수주 품목 전체 삭제
    void deletedContractAllMaterials(String contractCode);

    // 수주 품목 삭제
    void deletedContractMaterials(String contractMaterialKey);

    // 수주 실행
    void startContract(String contractCode);

    // 창고키, 품목키를 활용하여 창고 수량 가지고 오기
    String getAvailableInventoryAsString(@Param("productKey") String productKey,
                                         @Param("warehouseKey") String warehouseKey);

    // 창고 수량 업데이트
    void updateInventoryAsString(@Param("productKey") String productKey,
                                 @Param("warehouseKey") String warehouseKey,
                                 @Param("updatedInventoryStr") String updatedInventoryStr);

    // 수주 실행될 때 나머지 완제품 밑에 있는 사내 생산품 insert
    void insertMaterial(ContractMaterialDBDTO materialDBDTO);

    String selectContractMaterialKey(ContractMaterialDBDTO materialDTO);

    // 감소된 값을 outgoing테이블에 저장
    void insertOutgoingRecord(@Param("productKey") String productKey,
                              @Param("warehouseKey") String warehouseKey,
                              @Param("materialKey") String materialKey,
                              @Param("releaseType") String releaseType,
                              @Param("planDate") String planDate,
                              @Param("quantity") String quantity,
                              @Param("note") String note);

    // 수주 완료
    void completeContract(String contractCode);

    // 수주 품목 리스트
    List<MaterialsInfoDTO> getMaterialInfoList();
}
