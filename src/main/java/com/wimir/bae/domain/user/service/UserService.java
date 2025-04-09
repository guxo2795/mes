package com.wimir.bae.domain.user.service;

import com.wimir.bae.domain.user.dto.UserInfoDTO;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.user.dto.UserRegDTO;
import com.wimir.bae.domain.user.mapper.UserMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.utils.CryptUtil;
import com.wimir.bae.global.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CryptUtil cryptUtil;

    // 유저 등록
    public void createUser(UserLoginDTO userLoginDTO, UserRegDTO regDTO) {

        // 유저 존재 확인
        validateUserExists(regDTO.getUserCode());

        // 비밀번호 유효성 확인
        ValidationUtil.isvalidPassword(regDTO.getPassword());
        String encodedPassword = passwordEncoder.encode(regDTO.getPassword());

        // 전화번호 유효성 확인
        ValidationUtil.isvalidPhoneNumber(regDTO.getPhoneNumber());
        String phoneNumber = cryptUtil.encrypt(regDTO.getPhoneNumber());

        regDTO.setPassword(encodedPassword);
        regDTO.setPhoneNumber(phoneNumber);

        userMapper.createUser(regDTO);
    }

    // 유저 목록 조회
    public List<UserInfoDTO> getUserList(UserLoginDTO userLoginDTO) {
        
        // getUserList()가 null 이면 예외처리를 하지않고 비어있는 리스트를 반환
        List<UserInfoDTO> list = Optional.ofNullable(userMapper.getUserList())
                                         .orElse(Collections.emptyList());

        // 전화번호 복호화
        list.stream()
                .filter(dto -> dto.getPhoneNumber() != null)
                .forEach(dto -> dto.setPhoneNumber(cryptUtil.decrypt(dto.getPhoneNumber())));

        return list;
    }

    private void validateUserExists(String userCode) {
        if(userMapper.isUserExist(userCode)) {
            throw new CustomRuntimeException("이미 존재하는 아이디입니다.");
        }
    }
}
