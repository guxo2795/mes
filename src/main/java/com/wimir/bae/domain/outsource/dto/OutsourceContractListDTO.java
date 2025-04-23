package com.wimir.bae.domain.outsource.dto;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class OutsourceContractListDTO {

    // 수주 정보
    private ContractInfoDTO contractInfo;

    // 수주 관련 모든 정보 DTO
    private List<OutsourceItemDTO> outsourceItemList;
}
