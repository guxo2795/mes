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
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
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

    public void deleteBom(UserLoginDTO userLoginDTO, List<String> bomKeyList) {
        List<BomInfoDTO> bomInfoDTOList = bomMapper.getBomInfoList(bomKeyList);

        if(bomInfoDTOList.size() != bomKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 bom이 포함되어 있습니다.");
        }

        List<Map<String, String>> materialRootKeyList = bomInfoDTOList.stream()
                .map(bomInfo -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("materialKey", bomInfo.getMaterialKey());
                    map.put("rootKey", bomInfo.getRootKey());
                    return map;
                })
                .collect(Collectors.toList());

        // bom 하위 품목이 있는 지 검사
        if(bomMapper.isBomChildList(materialRootKeyList)) {
            throw new CustomRuntimeException("하위 품목이 포함되어있는 bom이 존재합니다.");
        }

        bomMapper.deleteBomList(bomKeyList);
    }
}
