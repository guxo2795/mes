package com.wimir.bae.domain.outsource.mapper;

import com.wimir.bae.domain.outsource.dto.OutsourceRegDTO;
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
}
