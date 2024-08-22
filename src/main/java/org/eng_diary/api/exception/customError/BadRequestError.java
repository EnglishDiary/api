package org.eng_diary.api.exception.customError;

public class BadRequestError extends RuntimeException {

    public BadRequestError(String message) {
        super(message);
    }

    public BadRequestError(String message, Throwable cause) {
        super(message, cause);
    }

}
