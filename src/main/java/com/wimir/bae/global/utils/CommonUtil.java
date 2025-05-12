package com.wimir.bae.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

public class CommonUtil {

    public static LinkedHashMap<String, Object> dtoConverter(Object dto) {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(dto, LinkedHashMap.class);

    }
}
