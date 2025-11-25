package org.vovgoo.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.dto.exception.ExceptionResponse;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler({
            UserBlockedException.class,
            UserDeactivatedException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleUserStatusForbidden(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI()));
    }
    
    @ExceptionHandler({
            UserActiveException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleConflictActive(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ExceptionResponse<String>> handleUserServiceError(UserServiceException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
