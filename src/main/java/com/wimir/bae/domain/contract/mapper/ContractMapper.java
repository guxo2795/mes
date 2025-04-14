package com.wimir.bae.domain.contract.mapper;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractRegDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper {
    
    // 수주 등록
    void createContract(ContractRegDTO contractRegDTO);

    // 자재 수주 품목 등록
    void createContractMaterials(ContractRegDTO contractRegDTO);

    // 수주 목록 조회
    List<ContractInfoDTO> getContractList();
}
