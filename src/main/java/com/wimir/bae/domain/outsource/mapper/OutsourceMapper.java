package com.wimir.bae.domain.outsource.mapper;

import com.wimir.bae.domain.outsource.dto.OutsourceRegDTO;
import com.wimir.bae.domain.outsource.dto.OutsourceSearchInfoDTO;
import com.wimir.bae.domain.outsource.dto.OutsourceUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;

@Mapper
public interface OutsourceMapper {
    // 외주 중복 검사
    Boolean isExistOutsource(@Param("contractKey") String contractKey,
                             @Param("productKey") String productKey);

    // 외주 등록    
    void createOutsource(OutsourceRegDTO outsourceRegDTO);

    // 외주 정보
    OutsourceSearchInfoDTO searchOutsourceInfo(String outsourceKey);

    // 외주 수정
    void updateOutsource(OutsourceUpdateDTO outsourceUpdateDTO);
}
