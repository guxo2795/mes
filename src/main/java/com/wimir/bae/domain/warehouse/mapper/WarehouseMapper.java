package com.wimir.bae.domain.warehouse.mapper;

import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarehouseMapper {
    
    // 창고 명 중복 확인
    boolean isWarehouseNameExist(String warehouseName);

    // 창고 등록
    void createWarehouse(WarehouseRegDTO warehouseRegDTO);
}
