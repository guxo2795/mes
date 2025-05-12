package com.wimir.bae.domain.common.main.service;

import com.wimir.bae.domain.common.main.dto.CommonMainInfoDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainModDTO;
import com.wimir.bae.domain.common.main.dto.CommonMainSearchDTO;
import com.wimir.bae.domain.common.main.mapper.CommonMainMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
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
    public List<CommonMainInfoDTO> getCommonMainList(CommonMainSearchDTO searchDTO) {

        searchDTO.setOffset(
                PagingUtil.getPagingOffset(
                        searchDTO.getPage(),
                        searchDTO.getRecord()
                )
        );
        searchDTO.setSort(SortUtil.getDBSortStr(searchDTO.getSort()));

        return Optional.ofNullable(commonMainMapper.getCommonMainList(searchDTO))
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

    public void deleteCommonMain(List<String> commonKeyList) {

        // 이렇게 하면 삭제 1번 기능 => select 1번 + delete 1번 = 2번
        // 100개 삭제= >select 100번 + delete 100번 = 200번
        // N+1문제
        // 리스트 전체를 쿼리 1번을 보냄
        // 리스트 크기비교로 유효성 검사
        // 삭제쿼리도 리스트 전체를 1번 보냄

//        for(String commonKey : commonKeyList) {
//            CommonMainInfoDTO commonMainInfoDTO = commonMainMapper.getCommonMainByKey(commonKey);
//
//            validateCommonMainExists(commonMainInfoDTO);
//
//            commonMainMapper.deleteCommonMain(commonKey);
//        }

        List<CommonMainInfoDTO> commonMainInfoDTOList = commonMainMapper.getCommonMainInfoList(commonKeyList);
        // 유효성 검증
        if(commonMainInfoDTOList.size() != commonKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 상위 공통 코드가 포함되어 있습니다.");
        }

        commonMainMapper.deleteCommonMainList(commonKeyList);

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
