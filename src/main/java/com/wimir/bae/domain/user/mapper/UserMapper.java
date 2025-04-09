package com.wimir.bae.domain.user.mapper;

import com.wimir.bae.domain.user.dto.UserInfoDTO;
import com.wimir.bae.domain.user.dto.UserLoginInfoDTO;
import com.wimir.bae.domain.user.dto.UserModDTO;
import com.wimir.bae.domain.user.dto.UserRegDTO;
import org.apache.ibatis.annotations.Mapper;

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
    String getPasswordByUserKeyAndUserCode(String userKey, String userCode);

    // 토큰 기간 갱신
    void updateUserTokenDate(String userKey, String tokenDate, String userCode);

    // 토큰 날짜 조회
    UserLoginInfoDTO getUserTokenDate(String userKey, String tokenDate);

    // 사원 권한
    String getUserClass(String userKey);

    List<String> getUserClasses(List<String> list);

    // 유저 목록 조회
    List<UserInfoDTO> getUserList();

    // 유저 수정
    void updateUser(UserModDTO modDTO);

    // 유저 삭제
    void deleteUser(String userKey);
}

