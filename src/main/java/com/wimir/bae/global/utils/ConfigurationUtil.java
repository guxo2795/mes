package com.wimir.bae.global.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationUtil {

    @Getter
    private static String resourcePath;

    @Value("${resource.path}")
    public void setResourcePath(String path) {
        ConfigurationUtil.resourcePath = path;
    }
}
