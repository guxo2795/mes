package com.wimir.bae.global.exception;

import com.wimir.bae.global.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // 모든 에러는 200으로 변환, result 는 0으로 전송

    // Runtime Error
    @ExceptionHandler({CustomRuntimeException.class})
    public ResponseEntity<?> customRuntimeException(CustomRuntimeException e) {
        log.warn("Runtime Error", e);
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // Token Error
    @ExceptionHandler({CustomTokenException.class})
    public ResponseEntity<?> customTokenException(CustomTokenException e) {
        log.warn("Token Error", e);
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(-1)
                        .message(e.getMessage())
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    // AccessDeny Error
    @ExceptionHandler({CustomAccessDenyException.class})
    public ResponseEntity<?> customAccessDenyException(CustomAccessDenyException e) {
        log.warn("Access Deny Error", e);
        String message = "권한이 부족합니다. 다시 시도해 주세요.";
        ResponseDTO<String> responseDTO =
                ResponseDTO.<String>builder()
                        .result(0)
                        .message(message)
                        .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
