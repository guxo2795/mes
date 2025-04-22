package com.wimir.bae.domain.incoming.service;

import com.wimir.bae.domain.incoming.dto.IncomingMaterialInfoDTO;
import com.wimir.bae.domain.incoming.dto.IncomingMaterialSearchDTO;
import com.wimir.bae.domain.incoming.dto.IncomingQuantityDTO;
import com.wimir.bae.domain.incoming.dto.IncomingRegDTO;
import com.wimir.bae.domain.incoming.mapper.IncomingMapper;
import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.service.InventoryProductService;
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
public class IncomingService {

    private final IncomingMapper incomingMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    private final InventoryProductService inventoryProductService;

    public void createIncoming(UserLoginDTO userLoginDTO, IncomingRegDTO incomingRegDTO) {

        // 자재 입고(M2)
        if("M2".equals(incomingRegDTO.getIncomingTypeFlag())) {

            OrderItemInfoDTO orderItemInfo = orderItemMapper.getOrderItemInfo(incomingRegDTO.getOrderMaterialKey());
            OrderInfoDTO orderInfo = orderMapper.getOrderInfo(orderItemInfo.getOrderKey());
            if(!"0".equals(orderInfo.getIsCompleted())){
                throw new CustomRuntimeException("종결된 발주는 품목을 관리할 수 없습니다.");
            }

            IncomingQuantityDTO quantityDTO = incomingMapper.getQuantitySum(incomingRegDTO.getOrderMaterialKey());
            double regQuantity = Double.parseDouble(incomingRegDTO.getQuantity());
            double arrivedQuantity = quantityDTO.getArrivedQuantity();
            double inboundedQuantity = quantityDTO.getInboundedQuantity();
            arrivedQuantity = arrivedQuantity - inboundedQuantity;

            if(arrivedQuantity < regQuantity){
                throw new CustomRuntimeException("입고 가능 수량보다 수량이 많습니다.");
            }

            incomingRegDTO.setProductKey(orderItemInfo.getMaterialKey());
            incomingRegDTO.setUserCode(userLoginDTO.getUserCode());
            incomingMapper.createIncoming(incomingRegDTO);
        } else {
            throw new CustomRuntimeException("입고 플래그를 다시 확인해주세요.");
        }
        
        // 재고 증가
        InventoryCorrectionDTO correctionDTO = InventoryCorrectionDTO.builder()
                .productKey(incomingRegDTO.getProductKey())
                .warehouseKey(incomingRegDTO.getWarehouseKey())
                .quantity(incomingRegDTO.getQuantity())
                .note(incomingRegDTO.getNote())
                .build();
        inventoryProductService.increaseProductInventory(userLoginDTO, correctionDTO);
    }

    @Transactional(readOnly = true)
    public List<IncomingMaterialInfoDTO> getIncomingMaterialList(IncomingMaterialSearchDTO incomingMaterialSearchDTO) {
        return Optional.ofNullable(incomingMapper.getIncomingMaterialList(incomingMaterialSearchDTO))
                .orElse(Collections.emptyList());
    }
}
