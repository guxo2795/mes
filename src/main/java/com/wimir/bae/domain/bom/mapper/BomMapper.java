package com.wimir.bae.domain.bom.mapper;

import com.wimir.bae.domain.bom.dto.BomRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BomMapper {
    
    // BOM 중복 검사
    boolean isExistBom(String finishedKey, String materialKey);

    // BOM 등록
    void createBom(BomRegDTO regDTO);
}
