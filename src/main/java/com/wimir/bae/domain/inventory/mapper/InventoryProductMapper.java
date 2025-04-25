package com.wimir.bae.domain.inventory.mapper;

import com.wimir.bae.domain.inventory.dto.InventoryCorrectionDTO;
import com.wimir.bae.domain.inventory.dto.InventoryProductInfoDBDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InventoryProductMapper {

    // 품목, 창고 등록 시 초기 재고 설정(품목 새로 등록 시)
    void setInitialInventory(@Param("productKey") String productKey,
                             @Param("warehouseKey") String warehouseKey);

    // 자재, 완제품 재고 조회
    double getProductInventory(@Param("productKey") String productKey,
                               @Param("warehouseKey") String warehouseKey);

    // 자재, 완제품 재고 정정
    void setProductInventoryCorrection(InventoryCorrectionDTO regDTO);

    // 자재, 완제품 재고 현황
    List<InventoryProductInfoDBDTO> getProductInventoryStatus();
}
