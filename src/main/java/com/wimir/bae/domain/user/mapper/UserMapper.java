package com.wimir.bae.domain.user.mapper;

import com.wimir.bae.domain.user.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    // 유저 존재 확인
    boolean isUserExistByCode(String userCode);

    boolean isUserExistByKey(String userKey);

    // 유저 등록
    void createUser(UserRegDTO regDTO);

    // userCode로 userKey 조회
    String getUserKeyByUserCode(String userCode);

    // userKey, userCode로 password 조회
    String getPasswordByUserKeyAndUserCode(
            @Param("userKey") String userKey,
            @Param("userCode") String userCode
    );

    // 토큰 기간 갱신
    void updateUserTokenDate(
            @Param("userKey") String userKey,
            @Param("tokenDate") String tokenDate,
            @Param("userCode") String userCode
    );

    // 토큰 날짜 조회
    UserLoginInfoDTO getUserTokenDate(
            @Param("userKey") String userKey,
            @Param("tokenDate") String tokenDate
    );

    // 사원 권한
    String getUserClass(String userKey);

    List<String> getUserClasses(List<String> list);

    // 유저 목록 조회
    List<UserInfoDTO> getUserList(UserSearchDTO searchDTO);

    // 유저 수정
    void updateUser(UserModDTO modDTO);

    // 유저 삭제
    void deleteUser(String userKey);

    // 로그아웃
    void logout(String userCode);

    // 사원 수
    int getUserCount(UserSearchDTO searchDTO);

    // 비밀번호 변경
    void updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO);
}

