package com.wimir.bae.global.excel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExcelController {

    @GetMapping("/excel")
    public String excelDownloadPage() {
        return "excel"; // src/main/resources/templates/excel.html
    }
}
