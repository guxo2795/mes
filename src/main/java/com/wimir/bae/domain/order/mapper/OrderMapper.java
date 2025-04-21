package com.wimir.bae.domain.order.mapper;

import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderRegDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    
    // 자재 발주 등록
    void createOrder(OrderRegDTO orderRegDTO);

    // 자재 발주 목록
    List<OrderInfoDTO> getOrderList();
}
