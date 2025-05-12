package com.wimir.bae.global.excel.mapper;

import com.wimir.bae.global.excel.dto.ExcelInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExcelMapper {
    List<ExcelInfoDTO> getExcelList();

    String getConversionValue(@Param("engColumn") String engColumn,
                              @Param("value") String value);
}
