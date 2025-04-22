package com.wimir.bae.domain.order.service;

import com.wimir.bae.domain.incoming.dto.IncomingQuantityDTO;
import com.wimir.bae.domain.incoming.mapper.IncomingMapper;
import com.wimir.bae.domain.order.dto.OrderInfoDTO;
import com.wimir.bae.domain.order.dto.OrderItemInfoDTO;
import com.wimir.bae.domain.order.dto.OrderItemRegDTO;
import com.wimir.bae.domain.order.dto.OrderItemRegDetailDTO;
import com.wimir.bae.domain.order.mapper.OrderItemMapper;
import com.wimir.bae.domain.order.mapper.OrderMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final IncomingMapper incomingMapper;

    public void createOrderItem(UserLoginDTO userLoginDTO, OrderItemRegDTO orderItemRegDTO) {

        String orderKey = orderItemRegDTO.getOrderKey();
        // 발주 정보
        OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderKey);
        // 등록, 수정할 자재 목록
        List<OrderItemRegDetailDTO> orderItemRegDetailList = orderItemRegDTO.getList();

        // 발주 완료 여부
        if(!"0".equals(orderInfo.getIsCompleted())) {
            throw new CustomRuntimeException("발주가 완료된 품목은 관리할 수 없습니다.");
        }

        // 기존에 등록된 발주 품목
        List<OrderItemInfoDTO> orderItemList = orderItemMapper.getOrderItemList(orderKey);

        // 기존에 등록된 품목 keyList: 수정 or 유지
        List<String> regOrderMaterialKeyList = orderItemRegDetailList.stream()
                .map(OrderItemRegDetailDTO::getOrderMaterialKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 삭제되는 발주 품목 List
        // 기존에 등록된 품목 keyList에 포함되지 않는 key들은 삭제 list에 추가
        // => 그럼 등록만 하고싶을때는 기존 품목은 전부 삭제되는건가?
        // => no, 기존품목을 유지하고싶으면 그 key를 보내면됨. => '전체 갱신 방식'
        List<String> deleteOrderMaterialKeyList = orderItemList.stream()
                .map(OrderItemInfoDTO::getOrderMaterialKey)
                .filter(orderMaterialKey -> !regOrderMaterialKeyList.contains(orderMaterialKey))
                .collect(Collectors.toList());

        // 기존 발주 품목 삭제
        for (String orderMaterialKey : deleteOrderMaterialKeyList) {

            IncomingQuantityDTO quantityDTO = incomingMapper.getQuantitySum(orderMaterialKey);
            double arrivedQuantity = quantityDTO.getArrivedQuantity(); // 입하: 물건이 도착한 기록
            double inboundedQuantity = quantityDTO.getInboundedQuantity(); // 입고: 창고에 넣은 기록
            arrivedQuantity -= inboundedQuantity;

            if (arrivedQuantity > 0 || inboundedQuantity > 0) {
                throw new CustomRuntimeException("입하, 입고 기록이 있는 품목이 포함되어 있어 삭제할 수 없습니다.");
            }

            orderItemMapper.deleteOrderItem(orderMaterialKey);
        }

        // 자재 발주 품목 등록 or 수정
        for (OrderItemRegDetailDTO regDetailDTO : orderItemRegDetailList) {

            // 자재가 발주 업체에 포함된 품목인 지 검사
            List<ProductInfoDTO> productList = productMapper.getCompanyProductList(orderInfo.getCompanyKey());
            boolean hasProductInlist = productList.stream()
                    .anyMatch(dto -> dto.getProductKey().equals(regDetailDTO.getMaterialKey()));
            
            if(!hasProductInlist) {
                throw new CustomRuntimeException("업체에 포함된 자재가 아닙니다.");
            }

            // orderMaterialKey가 null이면 등록 else 수정
            // 프론트에서 입력으로 orderMaterialKey를 입력하지않으면 정상 동작x
            if(regDetailDTO.getOrderMaterialKey() == null) {
                orderItemMapper.createOrderItem(orderKey, regDetailDTO);
            } else {
                orderItemMapper.updateOrderItem(orderKey, regDetailDTO);
            }
            
        }

    }
}
