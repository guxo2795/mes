package com.wimir.bae.global.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationUtil {

    @Getter
    private static String resourcePath;
    @Getter
    private static String excelUnitKey;
    @Getter
    private static String excelProductTypeKey;

    @Value("${resource.path}")
    public void setResourcePath(String path) {
        ConfigurationUtil.resourcePath = path;
    }

    // 엑셀 업로드 Main 단위 Key
    @Value("${bae.excel.unitKey}")
    public void setExcelUnitKey(String path) {
        ConfigurationUtil.excelUnitKey = path;
    }

    // 엑셀 업로드 Main 분류(기종) Key
    @Value("${bae.excel.productTypeKey}")
    public void setExcelProductTypeKey(String path) {
        ConfigurationUtil.excelProductTypeKey = path;
    }
}
