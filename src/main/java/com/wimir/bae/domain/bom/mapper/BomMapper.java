package com.wimir.bae.domain.bom.mapper;

import com.wimir.bae.domain.bom.dto.BomInfoDTO;
import com.wimir.bae.domain.bom.dto.BomModDTO;
import com.wimir.bae.domain.bom.dto.BomRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface BomMapper {
    
    // BOM 중복 검사
    boolean isExistBom(String finishedKey, String materialKey);

    // BOM 등록
    void createBom(BomRegDTO regDTO);

    // BOM 목록
    List<BomInfoDTO> getBomList();

    // BOM 정보
    BomInfoDTO getBomInfo(String bomKey);

    // BOM 수정
    void updateBom(BomModDTO modDTO);
}
