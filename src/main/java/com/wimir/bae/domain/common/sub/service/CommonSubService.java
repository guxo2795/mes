package com.wimir.bae.domain.common.sub.service;

import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import com.wimir.bae.domain.common.sub.mapper.CommonSubMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonSubService {

    private final CommonMainMapper commonMainMapper;
    private final CommonSubMapper commonSubMapper;

    public void createCommonSub(UserLoginDTO userLoginDTO, CommonSubRegDTO regDTO) {

        // 상위 공통 코드의 is_immutable 확인 및 존재 여부 확인
        if(!commonMainMapper.canUpdateCommonMain(regDTO.getMainCommonKey())){
            throw new CustomRuntimeException("유효하지 않은 상위 공통 코드입니다.");
        }

        // 중복 확인 (대소문자 구분없이)
        if(commonSubMapper.isUpperLowerCaseDuplicate(regDTO.getMainCommonKey(), regDTO.getSubCommonName())){
            throw new CustomRuntimeException("이미 존재하는 하위 공통 코드입니다.");
        }

        commonSubMapper.createCommonSub(regDTO);
    }

    @Transactional(readOnly = true)
    public List<CommonSubInfoDTO> getCommonMainList() {

        return Optional.ofNullable(commonSubMapper.getCommonSubList())
                .orElse(Collections.emptyList());
    }
}
