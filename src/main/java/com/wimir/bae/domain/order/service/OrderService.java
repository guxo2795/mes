package com.wimir.bae.domain.order.service;

import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderRegDTO;
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
public class OrderService {

    private final OrderMapper orderMapper;
    private final CompanyMapper companyMapper;

    public void createOrder(UserLoginDTO userLoginDTO, OrderRegDTO orderRegDTO) {
        
        // 조달 업체(O)인 지 확인
        String companyTypeFlag = companyMapper.getCompanyTypeFlag(orderRegDTO.getCompanyKey());
        if(!"O".equals(companyTypeFlag)) {
            throw new CustomRuntimeException("조달 업체만 등록할 수 있습니다.");
        }

        orderMapper.createOrder(orderRegDTO);
    }

    @Transactional(readOnly = true)
    public List<OrderInfoDTO> getOrderList() {
        return Optional.ofNullable(orderMapper.getOrderList())
                .orElse(Collections.emptyList());
    }
}
