package com.wimir.bae.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserSearchDTO {

    // 페이지 번호
    @Pattern(regexp = "^(?:[1-9]\\d*|)$")
    private String page;

    // 페이지 당 데이터 수
    @Pattern(regexp = "^(?:[1-9]\\d*|)$")
    private String record;

    // sort=userName => 오름차순, -userName => 내림차순
    private String sort;

    // 데이터 시작 위치 지정: LIMIT 반환할 수 OFFSET 시작점
    private String offset;

    // 검색
    private String userName;
    private String userCode;
    private String departmentKey;
    private String positionKey;
    @Pattern(regexp = "^(U|A)?$")
    private String permissionTypeFlag;

    private String permission;

}
