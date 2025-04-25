package com.wimir.bae.domain.quality.service;

import com.wimir.bae.domain.quality.dto.FaultStateDTO;
import com.wimir.bae.domain.quality.dto.FaultStateModDTO;
import com.wimir.bae.domain.quality.mapper.FaultStateMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FaultStateService {

    private final FaultStateMapper faultStateMapper;

    @Transactional(readOnly = true)
    public List<FaultStateDTO> getFaultStateList() {
        return Optional.ofNullable(faultStateMapper.getFaultStateList())
                .orElse(Collections.emptyList());
    }

    public void updateFaultState(FaultStateModDTO modDTO, UserLoginDTO userLoginDTO) {
        if(!faultStateMapper.isFaultStateExist(modDTO.getFaultStateKey())) {
            throw new CustomRuntimeException("존재하지 않는 불량내역입니다.");
        }
        faultStateMapper.updateFaultState(modDTO);
    }
}
