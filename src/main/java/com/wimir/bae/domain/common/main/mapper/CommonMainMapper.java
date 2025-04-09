package com.wimir.bae.domain.common.main.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMainMapper {

    // 상위 공통 코드 중복 검사
    boolean isCommonMainExist(String mainCommonName);

    // 상위 공통 코드 등록
    void createCommonMain(String mainCommonName);
}
