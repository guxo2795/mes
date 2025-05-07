package com.wimir.bae.global.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SortUtil {

    public static String getDBSortStr(String sortStr) {

        if (sortStr == null || sortStr.isEmpty()) {
            return "";
        }

        return Arrays.stream(sortStr.split(","))
                .map(sort -> {
                    String column = convertToSnakeCase(sort.startsWith("-") ? sort.substring(1) : sort);
                    return sort.startsWith("-") ? column + " DESC" : column + " ASC";
                })
                .collect(Collectors.joining(", ", "ORDER BY ", ""));
    }

    public static String convertToSnakeCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return input
                .replaceAll(regex, replacement)
                .toLowerCase();
    }
}
