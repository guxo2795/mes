package com.wimir.bae.domain.inventory.service;

import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.mapper.InventoryProductMapper;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryProductService {

    private final InventoryProductMapper inventoryProductMapper;
    private final WarehouseMapper warehouseMapper;
    private final ProductMapper productMapper;

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
}
