package org.vovgoo.orderservice.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.dto.exception.ExceptionResponse;
import org.vovgoo.dto.exception.FieldErrors;
import org.vovgoo.orderservice.exception.custom.address.AddressDeletedException;
import org.vovgoo.orderservice.exception.custom.address.AddressNotFoundException;
import org.vovgoo.orderservice.exception.custom.address.AddressServiceException;
import org.vovgoo.orderservice.exception.custom.dish.DishNotAvailableException;
import org.vovgoo.orderservice.exception.custom.kafka.KafkaEventTypeMismatchException;
import org.vovgoo.orderservice.exception.custom.order.OrderNotFoundException;
import org.vovgoo.orderservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.orderservice.exception.custom.restaurant.RestaurantServiceException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            OrderNotFoundException.class,
            RestaurantNotFoundException.class,
            AddressNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler({
            DishNotAvailableException.class,
            AddressDeletedException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<FieldErrors>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldErrors fieldErrors = new FieldErrors(ex.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(fieldErrors, HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAuthDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.of("Требуется аутентификация", HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.of("Доступ запрещён", HttpStatus.FORBIDDEN, request.getRequestURI()));
    }

    @ExceptionHandler({
            AddressServiceException.class,
            RestaurantServiceException.class,
            KafkaEventTypeMismatchException.class,
            KafkaException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleInternal(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
