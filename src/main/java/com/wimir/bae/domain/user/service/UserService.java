package com.wimir.bae.domain.user.service;

import com.wimir.bae.domain.user.dto.UserInfoDTO;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.user.dto.UserModDTO;
import com.wimir.bae.domain.user.dto.UserRegDTO;
import com.wimir.bae.domain.user.mapper.UserMapper;
import com.wimir.bae.global.exception.CustomAccessDenyException;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.image.service.ImageService;
import com.wimir.bae.global.utils.CryptUtil;
import com.wimir.bae.global.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserMapper userMapper;

    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;
    private final CryptUtil cryptUtil;

    // 유저 등록
    public void createUser(UserLoginDTO userLoginDTO, UserRegDTO regDTO, MultipartFile imageFile, MultipartFile signatureFile) {

        // 유저 아이디 중복 확인
        validateDuplicateUserCode(regDTO.getUserCode());

        // 비밀번호 유효성 확인
        ValidationUtil.isvalidPassword(regDTO.getPassword());
        String encodedPassword = passwordEncoder.encode(regDTO.getPassword());

        // 전화번호 유효성 확인
        ValidationUtil.isvalidPhoneNumber(regDTO.getPhoneNumber());
        String phoneNumber = cryptUtil.encrypt(regDTO.getPhoneNumber());

        regDTO.setPassword(encodedPassword);
        regDTO.setPhoneNumber(phoneNumber);

        if (imageFile != null && !imageFile.isEmpty()) {
            regDTO.setUserImageKey(imageService.saveImage(imageFile));
        }

        if(signatureFile != null && !signatureFile.isEmpty()){
            regDTO.setUserSignatureKey(imageService.saveImage(signatureFile));
        }

        userMapper.createUser(regDTO);
    }

    // 유저 목록 조회
    @Transactional(readOnly = true)
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

    // 유저 수정
    public void updateUser(UserLoginDTO userLoginDTO,UserModDTO modDTO) {

        // 유저 권한 확인
        String userPermission = userLoginDTO.getUserClass();
        String modUserPermission = userMapper.getUserClass(modDTO.getUserKey());
        if("U".equals(userPermission) && "A".equals(modUserPermission)) {
            throw new CustomAccessDenyException();
        }

        // 유저 아이디 중복 확인
        validateDuplicateUserCode(modDTO.getUserCode());
        
        // 전화번호 유효성 확인 및 암호화
        ValidationUtil.isvalidPhoneNumber(modDTO.getPhoneNumber());
        String phoneNumber = cryptUtil.encrypt(modDTO.getPhoneNumber());
        modDTO.setPhoneNumber(phoneNumber);

        userMapper.updateUser(modDTO);
    }

    // 유저 삭제
    public void deleteUser(UserLoginDTO userLoginDTO, List<String> userKeyList) {

        // 유저 권한 확인
        String userPermission = userLoginDTO.getUserClass();
        if("U".equals(userPermission)) {
            List<String> userKeys = userMapper.getUserClasses(userKeyList);

            boolean containAdmin = userKeys.stream().anyMatch("A"::equals);

            if(containAdmin) {
                throw new CustomAccessDenyException();
            }
        }

        for (String userKey : userKeyList) {
            // 유저 존재 확인
            validateUserKeyExists(userKey);

            // 현재 유저 로그인 여부 확인
            if(Objects.equals(userMapper.getUserKeyByUserCode(userLoginDTO.getUserCode()), userKey)) {
                throw new CustomRuntimeException("로그인되어 있는 아이디는 삭제할 수 없습니다.");
            }

            // 유저 삭제
            userMapper.deleteUser(userKey);
        }
    }

    private void validateUserKeyExists(String userKey) {
        if(!userMapper.isUserExistByKey(userKey)) {
            throw new CustomRuntimeException("존재하지 않는 사원입니다.");
        }
    }
    
    private void validateDuplicateUserCode(String userCode) {
        if(userMapper.isUserExistByCode(userCode)) {
            throw new CustomRuntimeException("이미 존재하는 아이디입니다.");
        }
    }
}
