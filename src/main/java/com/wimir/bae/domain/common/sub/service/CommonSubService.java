package com.wimir.bae.domain.common.sub.service;

import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.domain.common.sub.dto.CommonSubInfoDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubModDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubRegDTO;
import com.wimir.bae.domain.common.sub.dto.CommonSubSearchDTO;
import com.wimir.bae.domain.common.sub.mapper.CommonSubMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.utils.PagingUtil;
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
    public List<CommonSubInfoDTO> getCommonSubList(CommonSubSearchDTO searchDTO) {
        searchDTO.setOffset(
                PagingUtil.getPagingOffset(
                        searchDTO.getPage(),
                        searchDTO.getRecord()));

        return Optional.ofNullable(commonSubMapper.getCommonSubList(searchDTO))
                .orElse(Collections.emptyList());
    }

    public void updateCommonSub(UserLoginDTO userLoginDTO, CommonSubModDTO modDTO) {

        // 하위 공통 코드 수정여부 확인
        // 하위 공통 코드의 is_deleted, is_immutable 확인
        // 상위 공통 코드의 is_immutable 확인
        // <choose>를 쓴 이유는?.. when 하나만 실행 => 범용성때문에
        // subCommonKey 가 비어있는 지 확인, mainCommonKey 가 비어있는 지 확인
        if(!commonSubMapper.canUpdateCommonSub(modDTO.getSubCommonKey())){
            throw new CustomRuntimeException("수정할 수 없는 하위 공통 코드 입니다.");
        }

        // 중복 확인
        CommonSubInfoDTO commonSubInfoDTO = commonSubMapper.getCommonSubInfo(modDTO.getSubCommonKey());
        if(!commonSubInfoDTO.getSubCommonName().equals(modDTO.getSubCommonName())
                && commonSubMapper.isUpperLowerCaseDuplicate(modDTO.getSubCommonKey(), commonSubInfoDTO.getSubCommonName())){
            throw new CustomRuntimeException("이미 존재하는 하위 공톹 코드 입니다.");
        }

        commonSubMapper.updateCommonSub(modDTO);
    }

    public void deleteCommonSub(UserLoginDTO userLoginDTO, List<String> subCommonKeyList) {

        // 존재 여부
        List<CommonSubInfoDTO> commonSubInfoDTOList = commonSubMapper.getCommonSubInfoList(subCommonKeyList);
        if(commonSubInfoDTOList.size() != subCommonKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 하위 공통 코드가 포함되어 있습니다.");
        }
        // 삭제 가능 여부
        if(!commonSubMapper.canUpdateCommonSubList(subCommonKeyList)) {
            throw new CustomRuntimeException("수정할 수 없는 하위 공통 코드가 포함되어 있습니다.");
        }

        commonSubMapper.deleteCommonSubList(subCommonKeyList);
    }
}
