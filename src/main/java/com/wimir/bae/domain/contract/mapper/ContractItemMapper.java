package com.wimir.bae.domain.contract.mapper;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractShipmentItemInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractItemMapper {

    // 수주 품목 정보
    List<ContractShipmentItemInfoDTO> getContractShipmentItemInfo(String contractKey);

    // 수주 정보 by 수주품목키
    ContractInfoDTO getContractCompleteInfo(@Param("materialKey") String materialKey);
}
