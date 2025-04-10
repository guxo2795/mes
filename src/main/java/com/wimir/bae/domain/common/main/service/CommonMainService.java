package com.wimir.bae.domain.common.main.service;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainModDTO;
import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public List<CommonMainInfoDTO> getCommonMainList() {

        return Optional.ofNullable(commonMainMapper.getCommonMainList())
                .orElse(Collections.emptyList());
    }

    public void updateCommonMain(CommonMainModDTO modDTO) {

        CommonMainInfoDTO commonMainInfoDTO = commonMainMapper.getCommonMainByKey(modDTO.getMainCommonKey());

        // 존재여부 확인
        validateCommonMainExists(commonMainInfoDTO);

        // 수정 가능 여부
        if(commonMainInfoDTO.getIsImmutable().equals("1")) {
            throw new CustomRuntimeException("해당 항목은 수정할 수 없습니다.");
        }

        // 중복 확인
        // 기존 이름과 바꿀 이름이 같지 않을때 -> db에 바꿀이름과 동일한 이름이 있는지 확인
        if(!commonMainInfoDTO.getMainCommonName().equals(modDTO.getMainCommonName())) {
            validateCommonMainName(modDTO.getMainCommonName());
        }

        commonMainMapper.updateCommonMain(modDTO);
    }

    private void validateCommonMainExists(CommonMainInfoDTO commonMainInfoDTO) {
        if(commonMainInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 상위 공통 코드입니다.");
        }
    }

    private void validateCommonMainName(String mainCommonName) {
        if(commonMainMapper.isCommonMainExist(mainCommonName))
            throw new CustomRuntimeException("이미 존재하는 상위 공통 코드입니다.");
    }
}
