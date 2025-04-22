package com.wimir.bae.domain.order.mapper;

import com.wimir.bae.domain.order.dto.OrderItemInfoDTO;
import com.wimir.bae.domain.order.dto.OrderItemRegDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface OrderItemMapper {
    
    // 자재 발주 품목 목록
    List<OrderItemInfoDTO> getOrderItemList(String orderKey);

    // 자재 발주 품목 삭제
    void deleteOrderItem(String orderMaterialKey);

    // 자재 발주 품목 등록
    void createOrderItem(@Param("orderKey") String orderKey,
                         @Param("regDTO") OrderItemRegDetailDTO regDTO);

    // 자재 발주 품목 수정(수량)
    void updateOrderItem(@Param("orderKey") String orderKey,
                         @Param("regDTO") OrderItemRegDetailDTO regDTO);

    // 자재 발주 품목 정보
    OrderItemInfoDTO getOrderItemInfo(String orderMaterialKey);
}
