package com.wimir.bae.global.utils;


import com.wimir.bae.global.exception.CustomRuntimeException;

public class PagingUtil {

    private static final int DEFAULT_RECORD_SIZE = 20;

    // 기본 10개 페이징
    public static String getPage(String page) {

        int record = 10;

        if (page == null || page.equals("null") || page.isEmpty()) return "";
        else {
            // 1 부터 시작해야하므로 0은 오류로 반환
            if (Integer.parseInt(page) == 0) throw new CustomRuntimeException("잘못된 페이징입니다.");

            return String.valueOf((Integer.parseInt(page) - 1) * record);
        }

    }

    /**
     * page 와 record 로 offset 구하는 함수
     * @param page 페이지 번호
     * @param record 한 페이지 당 개수
     * @return offset
     */
    public static String getPagingOffset(String page, String record) {

        int recordInt = DEFAULT_RECORD_SIZE;
        if(record != null && !record.equals("null") && !record.isEmpty()) {
            try{
                recordInt = Integer.parseInt(record);
                if(recordInt <= 0) throw new CustomRuntimeException("잘못된 레코드 수 입니다.");
            } catch (NumberFormatException e) {
                throw new CustomRuntimeException("레코드 수는 숫자여야 합니다.");
            }
        }

        int pageInt = 1;
        if(page != null && !page.equals("null") && !page.isEmpty()) {
            try {
                pageInt = Integer.parseInt(page);
                if(pageInt <= 0) throw new CustomRuntimeException("잘못된 페이지 번호입니다.");
            } catch (NumberFormatException e) {
                throw new CustomRuntimeException("페이지 번호는 숫자여야 합니다.");
            }
        }

        int offset = (pageInt - 1) * recordInt;
        return String.valueOf(offset);

    }
}
