package com.wimir.bae.domain.contract.service;

import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.dto.ContractRegDTO;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService {

    private final ContractMapper contractMapper;
    private final CompanyMapper companyMapper;

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
}
