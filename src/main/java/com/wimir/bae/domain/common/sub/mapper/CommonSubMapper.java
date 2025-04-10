package com.wimir.bae.domain.common.sub.mapper;

import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonSubMapper {

    // 하위 공통 코드 중복 검사 (대소문자 구분 없이)
    boolean isUpperLowerCaseDuplicate(
            @Param("mainCommonKey") String mainCommonKey,
            @Param("subCommonName") String subCommonName
    );

    // 하위 공통 코드 등록
    void createCommonSub(CommonSubRegDTO regDTO);

    // 하위 공통 코드 목록 조회
    List<CommonSubInfoDTO> getCommonSubList();
}
