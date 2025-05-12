package com.wimir.bae.global.excel.service;

import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.excel.dto.ExcelInfoDTO;
import com.wimir.bae.global.excel.mapper.ExcelMapper;
import com.wimir.bae.global.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ExcelMapper excelMapper;

    public SXSSFWorkbook createExcel(UserLoginDTO userLoginDTO, List<?> infoList) {

        List<String> headerList = new ArrayList<>();
        List<ExcelInfoDTO> excelList = excelMapper.getExcelList();

        // ObjectMapper 을 사용해서 LinkedHashMap 형태로 변환하기 위해서, infoList 의 객체를 Object 타입으로 받음.
        // 여러 DTO 클래스를 입력받기 위해서 Object 타입을 이용 => 유연성
        Object listDTO;
        listDTO = infoList.get(0);
        LinkedHashMap<?, ?> map = CommonUtil.dtoConverter(listDTO);

        // 엑셀에 출력될 engColumn 추가
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String column = entry.getKey().toString();
            if (excelOutputColumn(excelList, column)) {
                headerList.add(column);
            }
        }

        // 엑셀 파일, 시트, 헤더 생성
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();
        SXSSFRow headerRow = sheet.createRow(0);

        // 셀 스타일 지정
        CellStyle headerCellStyle = setCellStyle(workbook, "head");
        CellStyle commonCellStyle = setCellStyle(workbook, "common");

        // 헤더 설정
        for (int i = 0; i < headerList.size(); i++) {
            SXSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(headerCellStyle);
            
            // 영문 컬럼명 -> 한글 컬럼명 변환
            String headerValue = excelColumnConverter(excelList, headerList.get(i));
            headerCell.setCellValue(headerValue);

            // 열 최소 너비
            sheet.setColumnWidth(headerCell.getColumnIndex(), 3500);
        }
        
        // 데이터 삽입
        int rowNum = 1; // 헤더(0)를 생성했으므로 1부터 시작
//        int rowspan = 0;

        // 조회해온 데이터 리스트의 크기만큼 반복
        for (Object obj : infoList) {
            SXSSFRow row = sheet.createRow(rowNum++);
            int cellNum = 0;

            for (String key : headerList) {
                LinkedHashMap<?, ?> map2 = CommonUtil.dtoConverter(obj);
                Object cellValue = map2.get(key); // eng_column
                Object convertedValue = excelCellConverter(excelList, key, cellValue);

                SXSSFCell cell = row.createCell(cellNum++);
                cell.setCellStyle(commonCellStyle);

                // db에 저장된 형태가 숫자형이라면
                if(convertedValue instanceof Double){
                    cell.setCellValue((Double)convertedValue);
                } else {
                    cell.setCellValue(convertedValue.toString());
                }
            }
        }

        return workbook;
    }

    // List에 병합된 데이터가 있는지 확인
    private boolean hasMergedData(List<?> list) {
        for (Object item : list) {
            LinkedHashMap<?, ?> itemMap = CommonUtil.dtoConverter(item);

            for (Map.Entry<?, ?> entry : itemMap.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof ArrayList) {
                    return true;
                }
            }
        }
        return false;
    }

    // 엑셀에 출력될 컬럼 지정
    private boolean excelOutputColumn(List<ExcelInfoDTO> excelList, String engColumn) {

        return excelList.stream()
                .anyMatch(dto -> dto.getEngColumn().equals(engColumn));
    }

    // 셀 스타일 지정
    private CellStyle setCellStyle(SXSSFWorkbook workbook, String cellType) {

        // 셀 스타일 생성
        CellStyle cellStyle = workbook.createCellStyle();

        // 셀 스타일 설정
        if ("head".equals(cellType)) {
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    // 영문 컬럼 한글 컬럼으로 변경
    private String excelColumnConverter(List<ExcelInfoDTO> excelList, String engColumn) {

        Optional<ExcelInfoDTO> matchingDTO = excelList.stream()
                .filter(dto -> dto.getEngColumn().equals(engColumn))
                .findFirst();

        // 영문 컬럼명에 해당하는 DTO를 찾았다면 해당하는 한글 컬럼명 반환
        if (matchingDTO.isPresent()) {
            return matchingDTO.get().getKorColumn();
        }

        // 해당하는 DTO를 찾지 못했다면 기본적으로 영문 컬럼명 반환
        return engColumn;
    }

    // 엑셀 cell 값 변환
    private Object excelCellConverter(List<ExcelInfoDTO> excelList, String engColumn, Object cellValue) {

        if (cellValue == null) {
            return "";
        }

        // List에서 engColumn에 해당하는 ExcelInfoDTO 객체를 찾기
        Optional<ExcelInfoDTO> matchingColumn = excelList.stream()
                .filter(dto -> dto.getEngColumn().equals(engColumn))
                .findFirst();

        // 변환값이 존재하면 변환
        if (matchingColumn.isPresent()) {
            ExcelInfoDTO dto = matchingColumn.get();
            if (dto.getIsNumber().equals("1")) {
                try {
                    return Double.parseDouble(cellValue.toString());
                } catch (NumberFormatException e) {
                    return cellValue.toString();
                }
            } else if (dto.getIsConverted().equals("1")) {
                return excelMapper.getConversionValue(engColumn, cellValue.toString());
            } else {
                return cellValue.toString();
            }
        }

        return "엑셀 변환 중 오류 발생";
    }

}
