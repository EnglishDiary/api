package org.eng_diary.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class AuthErrorHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleUnauthorizedException(AuthorizationDeniedException e) {
        log.error("unauthorized access", e);

        ApiResponse<?> error = ApiResponse.error("인증에 실패하였습니다.");
        return new ResponseEntity<>(error, UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error("bad credentials", e);

        ApiResponse<?> error = ApiResponse.error("아이디 또는 비밀번호가 틀렸습니다.");
        return new ResponseEntity<>(error, BAD_REQUEST);
    }




}
