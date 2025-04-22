package com.wimir.bae.domain.incoming.service;

import com.wimir.bae.domain.incoming.dto.IncomingArrivalInfoDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalRegDTO;
import com.wimir.bae.domain.incoming.dto.IncomingArrivalSearchDTO;
import com.wimir.bae.domain.incoming.mapper.IncomingArrivalMapper;
import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderItemInfoDTO;
import com.wimir.bae.domain.order.mapper.OrderItemMapper;
import com.wimir.bae.domain.order.mapper.OrderMapper;
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
public class IncomingArrivalService {

    private final IncomingArrivalMapper incomingArrivalMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;


    public void createArrival(UserLoginDTO userLoginDTO, IncomingArrivalRegDTO incomingArrivalRegDTO) {

        OrderItemInfoDTO orderItemInfo = orderItemMapper.getOrderItemInfo(incomingArrivalRegDTO.getOrderMaterialKey());

        OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderItemInfo.getOrderKey());
        if(!"0".equals(orderInfo.getIsCompleted())){
            throw new CustomRuntimeException("종결된 발주는 입하 등록을 할 수 없습니다.");
        }

        incomingArrivalRegDTO.setProductKey(orderItemInfo.getMaterialKey());
        incomingArrivalRegDTO.setUserCode(userLoginDTO.getUserCode());
        incomingArrivalMapper.createArrival(incomingArrivalRegDTO);
    }

    @Transactional(readOnly = true)
    public List<IncomingArrivalInfoDTO> getArrivalList(IncomingArrivalSearchDTO incomingArrivalSearchDTO) {
        return Optional.ofNullable(incomingArrivalMapper.getArrivalList(incomingArrivalSearchDTO))
                .orElse(Collections.emptyList());
    }
}
