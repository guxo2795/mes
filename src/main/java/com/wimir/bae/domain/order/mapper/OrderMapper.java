package com.wimir.bae.domain.order.mapper;

import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderModDTO;
import com.wimir.bae.domain.order.dto.OrderRegDTO;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Mapper
public interface OrderMapper {
    
    // 자재 발주 등록
    void createOrder(OrderRegDTO orderRegDTO);

    // 자재 발주 목록
    List<OrderInfoDTO> getOrderList();

    // 자재 발주 정보
    OrderInfoDTO getOrderInfo(String orderKey);

    // 자재 발주 수정
    void updateOrder(OrderModDTO orderModDTO);

    // 자재 발주 삭제
    void deleteOrder(String orderKey);

    // 자재 발주 완료
    void completeOrder(String orderKey);
}
