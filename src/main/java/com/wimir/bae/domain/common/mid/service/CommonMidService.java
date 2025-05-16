package com.wimir.bae.domain.common.mid.service;

import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.domain.common.mid.dto.CommonMidInfoDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidModDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidRegDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidSearchDTO;
import com.wimir.bae.domain.common.mid.mapper.CommonMidMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.utils.PagingUtil;
import com.wimir.bae.global.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonMidService {

    private final CommonMainMapper commonMainMapper;
    private final CommonMidMapper commonMidMapper;

    public void createCommonMid(UserLoginDTO userLoginDTO, CommonMidRegDTO regDTO) {

        // main 공통코드가 존재하는 지 & is_immutable 확인
        if(!commonMainMapper.canUpdateCommonMain(regDTO.getMainCommonKey())){
            throw new CustomRuntimeException("유효하지 않은 상위 공통 코드입니다.");
        }

        // name 중복 확인
        if(commonMidMapper.isCommonMidExist(regDTO.getMidCommonName())){
            throw new CustomRuntimeException("이미 존재하는 중위 공통 코드입니다.");
        }

        commonMidMapper.createCommonMid(regDTO);
    }

    @Transactional(readOnly = true)
    public List<CommonMidInfoDTO> getCommonMidList(CommonMidSearchDTO searchDTO) {

        searchDTO.setOffset(
                PagingUtil.getPagingOffset(
                        searchDTO.getPage(),
                        searchDTO.getRecord()));

        searchDTO.setSort(SortUtil.getDBSortStr(searchDTO.getSort()));

        return Optional.ofNullable(commonMidMapper.getCommonMidList(searchDTO))
                .orElse(Collections.emptyList());
    }

    public void updateCommonMid(UserLoginDTO userLoginDTO, CommonMidModDTO modDTO) {

        if(!commonMidMapper.canUpdateCommonMid(modDTO.getMidCommonKey())){
            throw new CustomRuntimeException("수정할 수 없는 중위 공통 코드입니다.");
        }
        if(commonMidMapper.isCommonMidExist(modDTO.getMidCommonName())){
            throw new CustomRuntimeException("이미 존재하는 중위 공통 코드입니다.");
        }

        commonMidMapper.updateCommonMid(modDTO);
    }


    public void deleteCommonMid(UserLoginDTO userLoginDTO, List<String> midCommonKeyList) {

        List<CommonMidInfoDTO> commonMidInfoDTOList = commonMidMapper.getCommonMidInfoList(midCommonKeyList);
        if(commonMidInfoDTOList.size() != midCommonKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 중위 공통 코드가 포함되어 있습니다.");
        }
        if(!commonMidMapper.canUpdateCommonMidList(midCommonKeyList)){
            throw new CustomRuntimeException("수정할 수 없는 중위 공통 코드가 포함되어 있습니다.");
        }

        commonMidMapper.deleteCommonMidList(midCommonKeyList);
    }
}
