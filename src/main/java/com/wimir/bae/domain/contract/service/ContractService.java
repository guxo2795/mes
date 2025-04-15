package com.wimir.bae.domain.contract.service;

import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.contract.dto.*;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {

    private final ContractMapper contractMapper;
    private final CompanyMapper companyMapper;

    private String inHouse = "8";

    public void createContract(UserLoginDTO userLoginDTO, ContractRegDTO contractRegDTO) {

        String companyTypeFlag = companyMapper.getCompanyTypeFlag(contractRegDTO.getCompanyKey());
        if(!"S".equals(companyTypeFlag)){
            throw new CustomRuntimeException("납품 업체만 등록이 가능합니다.");
        }

        contractMapper.createContract(contractRegDTO);
        contractMapper.createContractMaterials(contractRegDTO);
    }

    @Transactional(readOnly = true)
    public List<ContractInfoDTO> getContractList() {
        return Optional.ofNullable(contractMapper.getContractList())
                .orElse(Collections.emptyList());
    }

    public void updateContract(UserLoginDTO userLoginDTO, ContractModDTO modDTO) {

        ContractInfoDTO infoDTO = contractMapper.getContractInfo(modDTO.getContractCode());
        if (!"0".equals(infoDTO.getIsCompleted())) {
            throw new CustomRuntimeException("이미 실행거나 종결된 수주는 수정 할 수 없습니다.");
        }

        String companyTypeFlag = companyMapper.getCompanyTypeFlag(modDTO.getCompanyKey());
        if (!"S".equals(companyTypeFlag)) {
            throw new CustomRuntimeException("납품 업체만 등록할 수 있습니다.");
        }

        contractMapper.updateContract(modDTO);
        contractMapper.updateContractMaterial(modDTO.getContractCode(),modDTO.getProductKey(),modDTO.getQuantity());
    }

    // 수주 전 자재를 가져오는 메서드
    @Transactional(readOnly = true)
    public List<ContractMaterialInfoDTO> listContractMaterial(ContractMaterialSearchDTO searchDTO) {
        return Optional.ofNullable(contractMapper.listContractMaterial(searchDTO.getContractCode(), inHouse))
                .orElse(Collections.emptyList());
    }
}
