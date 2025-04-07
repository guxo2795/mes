package com.wimir.bae.domain.user.mapper;

import com.wimir.bae.domain.user.dto.UserRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.Valid;

@Mapper
public interface UserMapper {
    
    // 유저 존재 확인
    boolean isUserExist(String userCode);

    // 유저 등록
    void createUser(UserRegDTO regDTO);
}

