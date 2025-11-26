package org.vovgoo.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.dto.exception.ExceptionResponse;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ExceptionResponse<String>> handleUserServiceError(UserServiceException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
    })
    public ResponseEntity<ExceptionResponse<String>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }
}
