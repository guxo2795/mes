package com.wimir.bae.domain.common.main.service;

import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonMainService {

    private final CommonMainMapper commonMainMapper;

    // 개발시에만 사용
    public void createCommonMain(String mainCommonName) {
        // commonMain 중복 확인
        validateCommonMainName(mainCommonName);

        commonMainMapper.createCommonMain(mainCommonName);
    }

    private void validateCommonMainName(String mainCommonName) {
        if(commonMainMapper.isCommonMainExist(mainCommonName))
            throw new CustomRuntimeException("이미 존재하는 상위 공통 코드입니다.");
    }
}
