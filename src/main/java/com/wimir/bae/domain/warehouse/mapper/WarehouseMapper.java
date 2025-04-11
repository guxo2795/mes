package com.wimir.bae.domain.warehouse.mapper;

import com.wimir.bae.domain.warehouse.dto.WarehouseInfoDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseModDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    
    // 창고 명 중복 확인
    boolean isWarehouseNameExist(String warehouseName);

    // 창고 등록
    void createWarehouse(WarehouseRegDTO warehouseRegDTO);

    // 창고 조회
    List<WarehouseInfoDTO> getWarehouseList();

    // 창고 정보
    WarehouseInfoDTO getWarehouseInfo(String warehouseKey);

    // 창고 수정
    void updateWarehouse(WarehouseModDTO modDTO);
}
