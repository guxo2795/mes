package com.wimir.bae.domain.order.mapper;

import com.wimir.bae.domain.order.dto.OrderItemInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    
    // 자재 발주 품목 목록
    List<OrderItemInfoDTO> getOrderItemList();

    // 자재 발주 품목 삭제
    void deleteOrderItem(String orderMaterialKey);
}
