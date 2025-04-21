package com.wimir.bae.domain.order.service;

import ch.qos.logback.core.util.TimeUtil;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.incoming.dto.IncomingQuantityDTO;
import com.wimir.bae.domain.incoming.mapper.IncomingMapper;
import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderItemInfoDTO;
import com.wimir.bae.domain.order.dto.OrderModDTO;
import com.wimir.bae.domain.order.dto.OrderRegDTO;
import com.wimir.bae.domain.order.mapper.OrderItemMapper;
import com.wimir.bae.domain.order.mapper.OrderMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderMapper orderMapper;
    private final CompanyMapper companyMapper;
    private final OrderItemMapper orderItemMapper;
    private final IncomingMapper incomingMapper;

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

    public void updateOrder(UserLoginDTO userLoginDTO, OrderModDTO orderModDTO) {
        
        // 조달 업체(O)인 지 확인
        String companyTypeFlag = companyMapper.getCompanyTypeFlag(orderModDTO.getCompanyKey());
        if (!"O".equals(companyTypeFlag)) {
            throw new CustomRuntimeException("조달 업체만 등록할 수 있습니다.");
        }

        // 종결 여부 검사
        OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderModDTO.getOrderKey());
        if (!"0".equals(orderInfo.getIsCompleted())) {
            throw new CustomRuntimeException("종결된 발주는 수정할 수 없습니다.");
        }

        OrderInfoDTO orderInfoDTO = orderMapper.getOrderInfo(orderModDTO.getOrderKey());

        // 발주, 납기 일자 유효성 검사
        try {
            boolean hasOrderDate = orderModDTO.getOrderDate() != null && !orderModDTO.getOrderDate().isBlank();
            boolean hasSendDate = orderModDTO.getSendDate() != null && !orderModDTO.getSendDate().isBlank();

            LocalDate orderDate = hasOrderDate ? LocalDate.parse(orderModDTO.getOrderDate()) : null;
            LocalDate sendDate = hasSendDate ? LocalDate.parse(orderModDTO.getSendDate()) : null;

            if(hasOrderDate && !hasSendDate) {
                // 발주 일자만 있는 경우
                if(orderInfoDTO.getSendDate() != null) {
                    LocalDate savedSendDate = LocalDate.parse(orderInfoDTO.getSendDate());
                    if(orderDate.isAfter(savedSendDate)) {
                        throw new CustomRuntimeException("기존 납기일보다 입력한 발주 일자가 이후입니다.");
                    }
                }
            } else if (!hasOrderDate && hasSendDate) {
                // 납기 일자만 있는 경우
                if(orderInfoDTO.getOrderDate() != null) {
                    LocalDate savedOrderDate = LocalDate.parse(orderInfoDTO.getOrderDate());
                    if(sendDate.isBefore(savedOrderDate)) {
                        throw new CustomRuntimeException("기존 발주일보다 입력한 납기 일자가 이전입니다.");
                    }
                }
            } else if (hasOrderDate && hasSendDate) {
                // 둘 다 있는 경우
                if (orderDate.isAfter(sendDate)) {
                    throw new CustomRuntimeException("입력한 납기일이 입력한 발주 일자보다 이전입니다.");
                }
            }

        } catch (DateTimeException e) {
            throw new CustomRuntimeException("존재하지 않는 날짜입니다.");
        }

        orderMapper.updateOrder(orderModDTO);
    }

    public void deleteOrder(UserLoginDTO userLoginDTO, List<String> list) {

        for(String orderKey : list) {

            // 종결 여부 검사
            OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderKey);
            if(!"0".equals(orderInfo.getIsCompleted())) {
                throw new CustomRuntimeException("종결된 발주는 삭제할 수 없습니다.");
            }
            
            // 발주 품목 중 입고되지 않은 수량이 있는 지 검사
            List<OrderItemInfoDTO> orderItemList = orderItemMapper.getOrderItemList();
            for(OrderItemInfoDTO orderItemInfoDTO : orderItemList) {
                IncomingQuantityDTO incomingQuantityDTO = incomingMapper.getQuantitySum(orderItemInfoDTO.getOrderMaterialKey());
                double arrivedQuantity = incomingQuantityDTO.getArrivedQuantity(); // 입하 수량
                double inboundedQuantity = incomingQuantityDTO.getInboundedQuantity(); // 입고 수량
                arrivedQuantity -= inboundedQuantity;

                // 입고 가능 수량이 남이있거나 입고 기록이 있을 경우
                if (arrivedQuantity > 0 || inboundedQuantity > 0) {
                    throw new CustomRuntimeException("입하, 입고 기록이 있는 품목이 포함되어 있어 삭제할 수 없습니다.");
                }

                // 해당 발주 품목 삭제
                orderItemMapper.deleteOrderItem(orderItemInfoDTO.getOrderMaterialKey());
            }

            orderMapper.deleteOrder(orderKey);
        }

    }

    public void completeOrder(UserLoginDTO userLoginDTO, List<String> orderKeyList) {

        for(String orderKey : orderKeyList) {

            OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderKey);
            if(!"0".equals(orderInfo.getIsCompleted())) {
                throw new CustomRuntimeException("이미 종결된 발주입니다.");
            }

            // 발주 품목 중 입고되지 않은 수량이 있는 지 검사
            List<OrderItemInfoDTO> orderItemList = orderItemMapper.getOrderItemList();
            for(OrderItemInfoDTO orderItemInfoDTO : orderItemList) {
                IncomingQuantityDTO incomingQuantityDTO = incomingMapper.getQuantitySum(orderItemInfoDTO.getOrderMaterialKey());

                // 발주 수량 체크
                if (incomingQuantityDTO.getInboundedQuantity() < Double.parseDouble(orderItemInfoDTO.getOrderQuantity())) {
                    throw new CustomRuntimeException("'" + orderItemInfoDTO.getMaterialName() + "' 품목의 입고 수량이 부족합니다.");
                }
            }

            orderMapper.completeOrder(orderKey);
        }
    }
}
