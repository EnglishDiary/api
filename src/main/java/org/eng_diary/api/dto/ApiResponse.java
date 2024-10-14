package org.eng_diary.api.dto;

import lombok.Getter;
import org.eng_diary.api.util.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class ApiResponse<T> {
    private static final String successMessage = "요청이 성공적으로 처리되었습니다.";

    private final boolean status;
    private final String message;
    private final T data;
    private PageInfo pageInfo;

    private ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(boolean status, String message, T data, PageInfo pageInfo) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pageInfo = pageInfo;
    }

    // 성공 응답 생성 (데이터만)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, successMessage, data));
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> successWithPaging(Page<T> page) {
        return ResponseEntity.ok(new ApiResponse<>(true, successMessage, page.getContent(), PageInfo.fromPage(page)));
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
