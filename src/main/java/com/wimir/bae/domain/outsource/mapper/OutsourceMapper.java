package com.wimir.bae.domain.outsource.mapper;

import com.wimir.bae.domain.outsource.dto.OutsourceItemDTO;
import com.wimir.bae.domain.outsource.dto.OutsourceRegDTO;
import com.wimir.bae.domain.outsource.dto.OutsourceSearchInfoDTO;
import com.wimir.bae.domain.outsource.dto.OutsourceUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

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

    // 외주 삭제
    void deleteOutsource(String outsourceKey);

    // 외주 생산 품목 리스트
    List<OutsourceItemDTO> getOutsourceItemList(@Param("contractCode") String contractCode,
                                                @Param("processOutsourcedKey") String processOutsourcedKey);
}
