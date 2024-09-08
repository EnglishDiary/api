package org.eng_diary.api.dto;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {
    private final boolean status;
    private final String message;
    private final T data;

    private ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 생성 (데이터만)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data));
    }

    // 성공 응답 생성 (메세지만)
    public static ResponseEntity<ApiResponse<?>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, null));
    }

    // 성공 응답 생성 (메세지와 데이터)
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    // 에러 응답 생성
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

}
