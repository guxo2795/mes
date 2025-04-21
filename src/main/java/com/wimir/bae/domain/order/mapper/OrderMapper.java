package com.wimir.bae.domain.order.mapper;

import com.wimir.bae.domain.order.dto.OrderRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    
    // 자재 발주 등록
    void createOrder(OrderRegDTO orderRegDTO);
}
