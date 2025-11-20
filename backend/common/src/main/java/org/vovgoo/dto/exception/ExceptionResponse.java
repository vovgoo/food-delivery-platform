package org.vovgoo.dto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse<T> {

    private final LocalDateTime timestamp;
    private final int statusCode;
    private final String error;
    private final T body;
    private final String path;

    private ExceptionResponse(T body, HttpStatus status, String path) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = status.value();
        this.error = status.getReasonPhrase();
        this.body = body;
        this.path = path;
    }

    public static <T> ExceptionResponse<T> of(T body, HttpStatus status, String path) {
        return new ExceptionResponse<>(body, status, path);
    }
}
