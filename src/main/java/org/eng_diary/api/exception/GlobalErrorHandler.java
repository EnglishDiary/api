package org.eng_diary.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("unknown error", e);

        ApiResponse<?> error = ApiResponse.error("알 수 없는 오류가 발생하였습니다.");
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    // TODO 250420 JWT 토큰 예외처리 (만료, 유효하지 않은 토큰값 등)

}
