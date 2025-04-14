package com.wimir.bae.domain.bom.service;

import com.wimir.bae.domain.bom.dto.BomInfoDTO;
import com.wimir.bae.domain.bom.dto.BomModDTO;
import com.wimir.bae.domain.bom.dto.BomRegDTO;
import com.wimir.bae.domain.bom.mapper.BomMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
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
public class BomService {

    private final BomMapper bomMapper;
    private final ProductMapper productMapper;

    public void createBom(UserLoginDTO userLoginDTO, BomRegDTO regDTO) {

        if(bomMapper.isExistBom(regDTO.getFinishedKey(), regDTO.getMaterialKey())) {
            throw new CustomRuntimeException("이미 등록된 품목입니다.");
        }

        ProductInfoDTO rootInfo = productMapper.getProductInfo(regDTO.getFinishedKey());
        ProductInfoDTO materialInfo = productMapper.getProductInfo(regDTO.getMaterialKey());

        if(!"F".equals(rootInfo.getAssetTypeFlag())) {
            throw new CustomRuntimeException("올바른 완제품을 선택해 주십시오.");
        }
        if(!"M".equals(materialInfo.getAssetTypeFlag())) {
            throw new CustomRuntimeException("올바른 자재를 선택해 주십시오.");
        }

        bomMapper.createBom(regDTO);
    }

    public List<BomInfoDTO> getBomList() {
        return Optional.ofNullable(bomMapper.getBomList())
                .orElse(Collections.emptyList());

    }

    public void updateBom(UserLoginDTO userLoginDTO, BomModDTO modDTO) {

        BomInfoDTO bomInfoDTD = bomMapper.getBomInfo(modDTO.getBomKey());
        if(bomInfoDTD == null) {
            throw new CustomRuntimeException("존재하지 않는 BOM입니다.");
        }
        
        // 수주 진행 여부 확인


        bomMapper.updateBom(modDTO);
    }
}
