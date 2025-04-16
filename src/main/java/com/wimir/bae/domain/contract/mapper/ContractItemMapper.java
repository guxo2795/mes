package com.wimir.bae.domain.contract.mapper;

import com.wimir.bae.domain.contract.dto.ContractShipmentItemInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractItemMapper {

    // 수주 품목 정보
    List<ContractShipmentItemInfoDTO> getContractShipmentItemInfo(String contractKey);
}
