package com.wimir.bae.global.utils;

import com.wimir.bae.global.exception.CustomRuntimeException;

public class ValidationUtil {
    
    // 비밀번호 유효성 검사
    public static void isvalidPassword(String password) {

        if(password == null || password.isEmpty()) {
            throw new CustomRuntimeException("비밀번호는 반드시 필요합니다.");
        }

        // 비밀번호 정규식 = 영문자, 숫자, 특수문자 최소 1개이상 && 글자수 8자 이상
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,}$";

        if(!password.matches(passwordRegex)) {
            throw new CustomRuntimeException("비밀번호는 8자리 이상 및 특수문자(~!@#$%^&*?), 대문자, 숫자를 반드시 하나 이상 포함해야합니다.");
        }
    }

    // 전화번호 유효성 검사
    public static void isvalidPhoneNumber(String phoneNumber) {

        if(phoneNumber == null || phoneNumber.isEmpty()) {
            throw new CustomRuntimeException("전화번호는 반드시 필요합니다.");
        }

        String PhoneNumberRegex = "^01[016789]\\d{7,8}$";

        if(!phoneNumber.matches(PhoneNumberRegex)) {
            throw new CustomRuntimeException("유효한 전화번호 형식이 아닙니다. 예: 01012345678");
        }
    }

    // 파일명 유효성 검사
    public static void isValidFileName(String fileName) {

        if(fileName == null || fileName.isEmpty()) {
            throw new CustomRuntimeException("파일명이 필요합니다.");
        }
        if (fileName.contains("\\") || fileName.contains("/") || fileName.contains(":") ||
                fileName.contains("*") || fileName.contains("?") || fileName.contains("\"") ||
                fileName.contains("<") || fileName.contains(">") || fileName.contains("|")) {
            throw new CustomRuntimeException("이름에 특수 문자 '\\, /, :, *, ?, \", <, >, |'는 지원하지 않습니다.");
        } // ＃§★ 이런 특수기호들은 어떻게 하지?
        if(fileName.length() > 200){
            throw new CustomRuntimeException("파일명은 200자 이내로 가능합니다.");
        }
    }
}
