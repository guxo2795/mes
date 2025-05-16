package com.wimir.bae.domain.common.mid.mapper;

import com.wimir.bae.domain.common.mid.dto.CommonMidInfoDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidModDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidRegDTO;
import com.wimir.bae.domain.common.mid.dto.CommonMidSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.Size;
import java.util.List;

@Mapper
public interface CommonMidMapper {
    
    // 중위 공통 코드 중복 검사
    boolean isCommonMidExist(String midCommonName);

    // 중위 공통 코드 등록
    void createCommonMid(CommonMidRegDTO regDTO);

    // 중위 공통 코드 변경가능 여부
    boolean canUpdateCommonMid(String midCommonKey);
    boolean canUpdateCommonMidList(List<String> midCommonKeyList);

    // 중위 공통 코드 목록 조회
    List<CommonMidInfoDTO> getCommonMidList(CommonMidSearchDTO searchDTO);
    List<CommonMidInfoDTO> getCommonMidInfoList(List<String> midCommonKeyList);

    // 중위 공통 코드 정보 조회
    CommonMidInfoDTO getCommonMidInfo(String midCommonKey);

    // 중위 공통 코드 수정
    void updateCommonMid(CommonMidModDTO modDTO);

    // 중위 공통 코드 삭제
    void deleteCommonMidList(List<String> midCommonKeyList);
}
