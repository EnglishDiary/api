package org.eng_diary.api.exception.customError;

public class OpenApiServerError extends RuntimeException {
    public OpenApiServerError(String message) {
        super(message);
    }

}
