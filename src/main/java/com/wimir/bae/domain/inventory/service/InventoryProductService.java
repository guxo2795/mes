package com.wimir.bae.domain.inventory.service;

import com.wimir.bae.domain.incoming.dto.IncomingRegDTO;
import com.wimir.bae.domain.incoming.mapper.IncomingMapper;
import com.wimir.bae.domain.inventory.dto.*;
import com.wimir.bae.domain.inventory.mapper.InventoryProductMapper;
import com.wimir.bae.domain.outgoing.dto.OutgoingDecreaseRegDTO;
import com.wimir.bae.domain.outgoing.mapper.OutgoingMapper;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseInfoDTO;
import com.wimir.bae.domain.warehouse.mapper.WarehouseMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryProductService {

    private final InventoryProductMapper inventoryProductMapper;
    private final WarehouseMapper warehouseMapper;
    private final ProductMapper productMapper;
    private final OutgoingMapper outgoingMapper;
    private final IncomingMapper incomingMapper;

    public void setInitialInventoryByProduct(String productKey) {

        List<WarehouseInfoDTO> warehouseList = warehouseMapper.getWarehouseList();
        if (warehouseList.isEmpty()) {
            throw new CustomRuntimeException("현재 등록된 창고가 없습니다. 창고를 등록 후 제품을 등록해 주세요.");
        }

        // 모든 창고에 대해 초기 재고 등록(품목 새로 등록 시)
        for (WarehouseInfoDTO warehouseDTO: warehouseList) {
            String warehouseKey = warehouseDTO.getWarehouseKey();
            inventoryProductMapper.setInitialInventory(productKey, warehouseKey);
        }
    }

    public void setInitialInventoryByWarehouse(String warehouseKey) {

        // 모든 제품 리스트 조회
        List<ProductInfoDTO> productList = productMapper.getProductList();

        for(ProductInfoDTO productDTO: productList) {
            inventoryProductMapper.setInitialInventory(productDTO.getProductKey(), warehouseKey);
        }
    }

    public void increaseProductInventory(UserLoginDTO userLoginDTO, InventoryCorrectionDTO inventoryCorrectionDTO) {

        String productKey = inventoryCorrectionDTO.getProductKey();
        String warehouseKey = inventoryCorrectionDTO.getWarehouseKey();
        double regQuantity = Double.parseDouble(inventoryCorrectionDTO.getQuantity());

        // 기존 재고
        double oldProductQuantity = inventoryProductMapper.getProductInventory(productKey, warehouseKey);

        inventoryCorrectionDTO.setQuantity(String.valueOf(regQuantity + oldProductQuantity));
        inventoryProductMapper.setProductInventoryCorrection(inventoryCorrectionDTO);
    }

    public void decreaseProductInventory(UserLoginDTO userLoginDTO, InventoryCorrectionDTO inventoryCorrectionDTO) {

        double oldProductQuantity = inventoryProductMapper.getProductInventory(
                inventoryCorrectionDTO.getProductKey(),
                inventoryCorrectionDTO.getWarehouseKey());

        inventoryCorrectionDTO.setQuantity(String.valueOf(oldProductQuantity - Double.parseDouble(inventoryCorrectionDTO.getQuantity())));
        inventoryProductMapper.setProductInventoryCorrection(inventoryCorrectionDTO);
    }

    @Transactional(readOnly = true)
    public List<InventoryProductInfoDTO> getProductInventoryStatus() {

        List<InventoryProductInfoDBDTO> productInventoryStatus =
                Optional.ofNullable(inventoryProductMapper.getProductInventoryStatus())
                        .orElse(Collections.emptyList());

        // InventoryProductInfoDBDTO 데이터를 이용해서 InventoryProductInfoDetailDTO 리스트 저장
//        {
//            "품목1": [
//            { warehouseKey: "1", warehouseName: "A창고", quantity: "10" },
//            { warehouseKey: "2", warehouseName: "B창고", quantity: "5" }
//            ],
//            "품목2": [
//            { warehouseKey: "1", warehouseName: "A창고", quantity: "3" }
//            ]
//        }
        Map<String, List<InventoryProductInfoDetailDTO>> warehouseMap = productInventoryStatus.stream()
                .collect(Collectors.groupingBy(InventoryProductInfoDBDTO::getProductKey,
                        Collectors.mapping(dto -> {
                            InventoryProductInfoDetailDTO warehouseDTO = new InventoryProductInfoDetailDTO();
                            warehouseDTO.setWarehouseKey(dto.getWarehouseKey());
                            warehouseDTO.setWarehouseName(dto.getWarehouseName());
                            warehouseDTO.setQuantity(dto.getQuantity());
                            return warehouseDTO;
                        }, Collectors.toList())));

        return productInventoryStatus.stream()
                .map(infoDBDTO-> {
                    InventoryProductInfoDTO resultDTO = new InventoryProductInfoDTO();
                    resultDTO.setProductKey(infoDBDTO.getProductKey());
                    resultDTO.setProductName(infoDBDTO.getProductName());
                    resultDTO.setProductCode(infoDBDTO.getProductCode());
                    resultDTO.setTotalQuantity(infoDBDTO.getTotalQuantity());
                    resultDTO.setUnitName(infoDBDTO.getUnitName());
                    resultDTO.setWarehouse(warehouseMap.get(infoDBDTO.getProductKey()));
                    return resultDTO;
                })
                .distinct()
                .toList();
    }

    public void moveProductWarehouse(UserLoginDTO userLoginDTO, InventoryProductInoutMoveDTO inventoryProductInoutMoveDTO) {

        String productKey = inventoryProductInoutMoveDTO.getProductKey();
        String oldWarehouseKey = inventoryProductInoutMoveDTO.getWarehouseKey();
        String newWarehouseKey = inventoryProductInoutMoveDTO.getNewWarehouseKey();
        String quantity = inventoryProductInoutMoveDTO.getQuantity();
        String note = inventoryProductInoutMoveDTO.getNote();

        ProductInfoDTO productInfo = productMapper.getProductInfo(productKey);
        if(productInfo == null) {
            throw new CustomRuntimeException("존재하지 않는 제품입니다.");
        }

        Pattern zeroPattern = Pattern.compile("^0+(\\.0+)?$");
        if(zeroPattern.matcher(quantity).matches()) {
            throw new CustomRuntimeException("수량을 제대로 입력해 주시기 바랍니다.");
        }
        if ("F".equals(productInfo.getAssetTypeFlag())) {
            Pattern positivePattern = Pattern.compile("^[1-9]\\d*$");
            if (!positivePattern.matcher(quantity).matches()) {
                throw new CustomRuntimeException("완제품은 정수 양수로만 입력 가능합니다.");
            }
        }

        if(oldWarehouseKey.equals(newWarehouseKey)) {
            throw new CustomRuntimeException("변경할 창고는 현재 창고와 달라야 합니다.");
        }

        double regProductQuantity = Double.parseDouble(quantity); // 이동 수량
        double oldProductQuantity = inventoryProductMapper.getProductInventory(productKey, oldWarehouseKey); // 기존 창고 재고
        double newProductQuantity = inventoryProductMapper.getProductInventory(productKey, newWarehouseKey); // 옮길 창고 재고

        if (regProductQuantity > oldProductQuantity) {
            throw new CustomRuntimeException("기존 창고에 존재하는 수량보다 이동되는 수량이 더 많습니다.");
        }

        // 동시성문제?
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTimeParse = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String dateTimeAfterOneSecond = dateTimeParse.plusSeconds(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 기존 창고 출고
        outgoingMapper.createOutgoing(OutgoingDecreaseRegDTO.builder()
                .productKey(productKey)
                .warehouseKey(oldWarehouseKey)
                .outgoingTypeFlag("W2")
                .correctionDateTime(dateTime)
                .quantity(String.valueOf(regProductQuantity))
                .note(note)
                .build());

        // 기존 창고 재고 정정
        inventoryProductMapper.setProductInventoryCorrection(InventoryCorrectionDTO.builder()
                .productKey(productKey)
                .warehouseKey(oldWarehouseKey)
                .quantity(String.valueOf(oldProductQuantity-regProductQuantity))
                .note(note)
                .build());

        // 이동 창고 입고
        incomingMapper.createIncoming(IncomingRegDTO.builder()
                .productKey(productKey)
                .warehouseKey(newWarehouseKey)
                .IncomingTypeFlag("W2")
                .correctionDateTime(dateTimeAfterOneSecond)
                .quantity(String.valueOf(regProductQuantity))
                .note(note)
                .build());
        
        // 이동 창고 재고 정정
        inventoryProductMapper.setProductInventoryCorrection(InventoryCorrectionDTO.builder()
                .productKey(productKey)
                .warehouseKey(newWarehouseKey)
                .quantity(String.valueOf(newProductQuantity+regProductQuantity))
                .note(note)
                .build());
    }

}
