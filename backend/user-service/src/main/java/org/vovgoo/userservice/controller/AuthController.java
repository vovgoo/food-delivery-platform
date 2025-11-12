package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vovgoo.userservice.dto.auth.request.LoginRequest;
import org.vovgoo.userservice.dto.auth.request.RegisterRequest;
import org.vovgoo.userservice.dto.auth.response.JwtResponse;
import org.vovgoo.userservice.dto.auth.internal.JwtPair;
import org.vovgoo.userservice.exception.dto.ExceptionResponse;
import org.vovgoo.userservice.service.security.AuthService;
import org.vovgoo.userservice.utils.CookieUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация, логин и обновление токена")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса (валидация)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:00:12.345",
                      "statusCode": 400,
                      "error": "Bad Request",
                      "body": {
                        "errors": [
                          { "field": "email", "messages": ["Email не может быть пустым", "Email должен быть корректным"] },
                          { "field": "password", "messages": ["Пароль не может быть пустым", "Пароль должен быть от 8 до 100 символов", "Пароль должен содержать латинские буквы, цифры и спецсимволы"] },
                          { "field": "fullName", "messages": ["Полное имя не может быть пустым", "Имя должно быть от 2 до 100 символов"] }
                        ]
                      },
                      "path": "/api/v1/auth/register"
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Email уже занят",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:01:00.123",
                      "statusCode": 409,
                      "error": "Conflict",
                      "body": "Пользователь с email already@example.com уже существует",
                      "path": "/api/v1/auth/register"
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:02:45.456",
                      "statusCode": 500,
                      "error": "Internal Server Error",
                      "body": "Внутренняя ошибка сервера",
                      "path": "/api/v1/auth/register"
                    }
                    """)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.register(registerRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }

    @Operation(summary = "Вход пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса (валидация)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:10:22.123",
                      "statusCode": 400,
                      "error": "Bad Request",
                      "body": {
                        "errors": [
                          { "field": "email", "messages": ["Email не может быть пустым", "Email должен быть корректным"] },
                          { "field": "password", "messages": ["Пароль не может быть пустым", "Пароль должен быть от 8 до 100 символов"] }
                        ]
                      },
                      "path": "/api/v1/auth/login"
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Неверный email или пароль",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:11:00.456",
                      "statusCode": 401,
                      "error": "Unauthorized",
                      "body": "Неверный email или пароль",
                      "path": "/api/v1/auth/login"
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:12:45.789",
                      "statusCode": 500,
                      "error": "Internal Server Error",
                      "body": "Внутренняя ошибка сервера",
                      "path": "/api/v1/auth/login"
                    }
                    """)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.login(loginRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }

    @Operation(summary = "Обновление access-токена по refresh-токену")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access-токен успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Refresh-токен отсутствует или недействителен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:20:12.345",
                      "statusCode": 401,
                      "error": "Unauthorized",
                      "body": "Refresh токен недействителен или истёк",
                      "path": "/api/v1/auth/refresh"
                    }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-11-12T16:21:45.456",
                      "statusCode": 500,
                      "error": "Internal Server Error",
                      "body": "Внутренняя ошибка сервера",
                      "path": "/api/v1/auth/refresh"
                    }
                    """)
                    )
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.extractRefreshToken(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JwtPair jwtPair = authService.refreshAccessToken(refreshToken);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }
}
