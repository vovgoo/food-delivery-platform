package org.vovgoo.restaurantservice.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.restaurantservice.exception.custom.dish.DishNotBelongsToRestaurantException;
import org.vovgoo.restaurantservice.exception.custom.dish.DishNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.image.*;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.dto.exception.ExceptionResponse;
import org.vovgoo.dto.exception.FieldErrors;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RestaurantNotFoundException.class,
            DishNotFoundException.class,
            ImageNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(ImageLimitExceededException.class)
    public ResponseEntity<ExceptionResponse<String>> handleImageLimitExceeded(ImageLimitExceededException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler({
            DishNotBelongsToRestaurantException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.of("Требуется аутентификация", HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.of("Доступ запрещён", HttpStatus.FORBIDDEN, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<FieldErrors>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldErrors fieldErrors = new FieldErrors(ex.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(fieldErrors, HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler({
            ImageReadException.class,
            ImageUploadException.class,
            ImageHandlerNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleImageExceptions(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
