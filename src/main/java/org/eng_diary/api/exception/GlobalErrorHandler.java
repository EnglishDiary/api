package org.eng_diary.api.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.eng_diary.api.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("User Token is Expired", e);

        ApiResponse<?> response = ApiResponse.error("토큰 유효시간이 만료되었습니다.");
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("unknown error", e);

        ApiResponse<?> error = ApiResponse.error("알 수 없는 오류가 발생하였습니다.");
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

}
