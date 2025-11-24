package org.vovgoo.userservice.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.dto.exception.ExceptionResponse;
import org.vovgoo.dto.exception.FieldErrors;
import org.vovgoo.userservice.exception.custom.address.AddressNotFound;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventSerializationException;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventTypeMismatchException;
import org.vovgoo.userservice.exception.custom.messaging.RedisSerializationException;
import org.vovgoo.userservice.exception.custom.role.RoleNotFoundException;
import org.vovgoo.userservice.exception.custom.jwt.InvalidJwtTokenException;
import org.vovgoo.userservice.exception.custom.auth.InvalidRefreshTokenException;
import org.vovgoo.userservice.exception.custom.jwt.TokenStrategyNotFoundException;
import org.vovgoo.userservice.exception.custom.user.*;
import org.vovgoo.userservice.exception.custom.verification.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            AddressNotFound.class,
            RoleNotFoundException.class,
            EntityNotFoundException.class,
            OtpNotFoundException.class,
            SignUpRequestNotFoundException.class,
            ChangePhoneRequestNotFoundException.class,
            ChangeEmailRequestNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponse<String>> handleInvalidRefreshToken(InvalidRefreshTokenException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.of("Требуется авторизация", HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.of("Доступ запрещен", HttpStatus.FORBIDDEN, request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<FieldErrors>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldErrors fieldErrors = new FieldErrors(ex.getBindingResult().getFieldErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(fieldErrors, HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler({
            InvalidOtpException.class,
            PasswordMismatchException.class,
            PasswordAlreadyUsedException.class,
            EmailAlreadyCurrentException.class,
            PhoneAlreadyCurrentException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            PhoneAlreadyExistsException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler({
            OtpAttemptsExceededException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleTooManyRequests(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS, request.getRequestURI()));
    }

    @ExceptionHandler({
            RabbitEventSerializationException.class,
            RabbitEventTypeMismatchException.class,
            RedisSerializationException.class,
            TokenStrategyNotFoundException.class,
            InvalidJwtTokenException.class,
            AmqpException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleInternal(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
