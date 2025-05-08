package com.wimir.bae.domain.common.sub.mapper;

import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubModDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubSearchDTO;
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
    List<CommonSubInfoDTO> getCommonSubList(CommonSubSearchDTO searchDTO);
    List<CommonSubInfoDTO> getCommonSubInfoList(List<String> subCommonKeyList);

    boolean canUpdateCommonSub(@Param("subCommonKey") String subCommonKey);
    boolean canUpdateCommonSubList(List<String> subCommonKeyList);

    // 하위 공통 코드 정보 조회
    CommonSubInfoDTO getCommonSubInfo(String subCommonKey);

    // 하위 공통 코드 수정
    void updateCommonSub(CommonSubModDTO modDTO);

    // 하위 공통 코드 삭제
    void deleteCommonSubList(List<String> subCommonKeyList);
}
