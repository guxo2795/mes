package com.wimir.bae.domain.common.main.mapper;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainModDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonMainMapper {

    // 상위 공통 코드 중복 검사
    boolean isCommonMainExist(String mainCommonName);

    // 상위 공통 코드 등록
    void createCommonMain(String mainCommonName);

    // 상위 공통 코드 목록 조회
    List<CommonMainInfoDTO> getCommonMainList();

    // 상위 공통 코드 조회
    CommonMainInfoDTO getCommonMainByKey(String mainCommonKey);

    List<CommonMainInfoDTO> getCommonMainInfoList(List<String> commonKeyList);

    // 상위 공통 코드 수정
    void updateCommonMain(CommonMainModDTO modDTO);

    // 상위 공통 코드 삭제
    void deleteCommonMainList(List<String> commonKeyList);

    // 상위 공통 코드 변경가능여부
    boolean canUpdateCommonMain(@Param("mainCommonKey") String mainCommonKey);
}
