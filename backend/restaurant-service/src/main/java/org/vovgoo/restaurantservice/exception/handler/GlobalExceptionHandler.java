package org.vovgoo.restaurantservice.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.restaurantservice.exception.custom.DishNotBelongsToRestaurantException;
import org.vovgoo.restaurantservice.exception.custom.DishNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.exception.dto.ExceptionResponse;
import org.vovgoo.restaurantservice.exception.dto.FieldErrors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ExceptionResponse<String>> handleRestaurantNotFound(RestaurantNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDishNotFound(DishNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(DishNotBelongsToRestaurantException.class)
    public ResponseEntity<ExceptionResponse<String>> handleDishNotBelongs(DishNotBelongsToRestaurantException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<FieldErrors>>  handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldErrors fieldErrors = new FieldErrors(ex.getBindingResult().getFieldErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(fieldErrors, HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse<String>>  handleRuntimeExceptions(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
