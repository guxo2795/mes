package com.wimir.bae.domain.warehouse.service;

import com.wimir.bae.domain.inventory.service.InventoryProductService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseInfoDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseModDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import com.wimir.bae.domain.warehouse.mapper.WarehouseMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;

    private final InventoryProductService inventoryProductService;

    public void createWarehouse(UserLoginDTO userLoginDTO, WarehouseRegDTO regDTO) {

        validateDuplicateWarehouseName(regDTO.getWarehouseName());

        warehouseMapper.createWarehouse(regDTO);
        
        // 초기 품목 정의 - inventory 도메인, 여기서 warehouseKey 사용
        inventoryProductService.setInitialInventoryByWarehouse(regDTO.getWarehouseKey());
    }

    public List<WarehouseInfoDTO> getWarehouseList() {
        return Optional.ofNullable(warehouseMapper.getWarehouseList())
                .orElse(Collections.emptyList());
    }

    public void updateWarehouse(UserLoginDTO userLoginDTO, WarehouseModDTO modDTO) {

        WarehouseInfoDTO warehouseInfoDTO = warehouseMapper.getWarehouseInfo(modDTO.getWarehouseKey());
        if(warehouseInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 창고입니다.");
        }

        if(!warehouseInfoDTO.getWarehouseName().equals(modDTO.getWarehouseName())) {
            validateDuplicateWarehouseName(modDTO.getWarehouseName());
        }

        warehouseMapper.updateWarehouse(modDTO);
    }

    public void deleteWarehouse(UserLoginDTO userLoginDTO, List<String> warehouseKeyList) {
        List<WarehouseInfoDTO> warehouseInfoDTOList = warehouseMapper.getWarehouseInfoList(warehouseKeyList);

        if(warehouseInfoDTOList.size() != warehouseKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 창고가 포함되어 있습니다.");
        }

        warehouseMapper.deleteWarehouseList(warehouseKeyList);
    }

    private void validateDuplicateWarehouseName(String warehouseName) {
        if (warehouseMapper.isWarehouseNameExist(warehouseName)) {
            throw new CustomRuntimeException("이미 존재하는 창고명입니다.");
        }
    }
}
