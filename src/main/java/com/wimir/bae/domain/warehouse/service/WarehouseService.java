package com.wimir.bae.domain.warehouse.service;

import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.domain.warehouse.dto.WarehouseRegDTO;
import com.wimir.bae.domain.warehouse.mapper.WarehouseMapper;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;

    public void createWarehouse(UserLoginDTO userLoginDTO, WarehouseRegDTO warehouseRegDTO) {

        if (warehouseMapper.isWarehouseNameExist(warehouseRegDTO.getWarehouseName())) {
            throw new CustomRuntimeException("이미 존재하는 창고명입니다.");
        }

        warehouseMapper.createWarehouse(warehouseRegDTO);
        
        // 초기 품목 정의 - inventory 도메인, 여기서 warehouseKey 사용
    }
}
