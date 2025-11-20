package org.vovgoo.userservice.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vovgoo.dto.exception.ExceptionResponse;
import org.vovgoo.dto.exception.FieldErrors;
import org.vovgoo.userservice.exception.custom.address.AddressNotFound;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventSerializationException;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventTypeMismatchException;
import org.vovgoo.userservice.exception.custom.messaging.RedisKeyTypeMismatchException;
import org.vovgoo.userservice.exception.custom.messaging.RedisSerializationException;
import org.vovgoo.userservice.exception.custom.role.RoleNotFoundException;
import org.vovgoo.userservice.exception.custom.security.InvalidJwtTokenException;
import org.vovgoo.userservice.exception.custom.security.InvalidRefreshTokenException;
import org.vovgoo.userservice.exception.custom.security.TokenStrategyNotFoundException;
import org.vovgoo.userservice.exception.custom.user.*;
import org.vovgoo.userservice.exception.custom.verification.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            AddressNotFound.class,
            RoleNotFoundException.class,
            EntityNotFoundException.class,
            EmailVerificationNotFoundException.class,
            OtpNotFoundException.class,
            SignUpRequestNotFoundException.class,
            ChangePhoneRequestNotFoundException.class,
            ChangeEmailRequestNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            InvalidRefreshTokenException.class,
            AuthorizationDeniedException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        String message = switch (ex.getClass().getSimpleName()) {
            case "BadCredentialsException" -> "Неверный телефон или пароль";
            case "AuthorizationDeniedException" -> "Требуется аутентификация";
            default -> ex.getMessage();
        };
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.of(message, HttpStatus.UNAUTHORIZED, request.getRequestURI()));
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
            EmailVerificationAttemptsExceededException.class,
            OtpAttemptsExceededException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleTooManyRequests(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS, request.getRequestURI()));
    }

    @ExceptionHandler({
            InvalidOtpException.class,
            PasswordMismatchException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ExceptionResponse<?>> handleBadRequest(Exception ex, HttpServletRequest request) {
        if (ex instanceof MethodArgumentNotValidException manv) {
            FieldErrors fieldErrors = new FieldErrors(manv.getBindingResult().getFieldErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ExceptionResponse.of(fieldErrors, HttpStatus.BAD_REQUEST, request.getRequestURI()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    @ExceptionHandler({
            RabbitEventSerializationException.class,
            RabbitEventTypeMismatchException.class,
            RedisKeyTypeMismatchException.class,
            RedisSerializationException.class,
            TokenStrategyNotFoundException.class,
            InvalidJwtTokenException.class
    })
    public ResponseEntity<ExceptionResponse<String>> handleInternalServerError(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}
