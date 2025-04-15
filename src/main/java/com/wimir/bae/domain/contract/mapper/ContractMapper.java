package com.wimir.bae.domain.contract.mapper;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractMaterialInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractModDTO;
import com.wimir.bae.domain.contract.dto.ContractRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface ContractMapper {
    
    // 수주 등록
    void createContract(ContractRegDTO contractRegDTO);

    // 수주 목록 조회
    List<ContractInfoDTO> getContractList();

    // 수주 정보
    ContractInfoDTO getContractInfo(String contractCode);

    // 수주 실행 시 품목 자재들 등록에 필요한 내용 불러오기
    List<ContractMaterialInfoDTO> listContractMaterial(String contractCode, String processTypeKey);

    // 수주 수정
    void updateContract(ContractModDTO modDTO);

    // 수주 품목 등록
    void createContractMaterials(ContractRegDTO contractRegDTO);

    // 수주 품목 수정
    void updateContractMaterial(String contractCode, String productKey, String quantity);

    // 수주 삭제
    void deletedContract(String contractCode);

    // 수주 품목 전체 삭제
    void deletedContractAllMaterials(String contractCode);
}
