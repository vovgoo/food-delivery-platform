package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.user.request.UserUpdateRequest;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.exception.dto.ExceptionResponse;
import org.vovgoo.userservice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Профиль и обновление данных пользователя")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить профиль текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "email": "user@example.com",
                                      "fullName": "Иван Иванов",
                                      "createdAt": "2025-11-12T16:00:12.345",
                                      "updatedAt": "2025-11-12T16:10:12.345",
                                      "roles": [{"id": 1, "name": "USER"}],
                                      "addresses": [
                                        {
                                          "id": 10,
                                          "street": "ул. Ленина, д.1",
                                          "city": "Москва",
                                          "zip": "101000",
                                          "state": "Москва",
                                          "country": "Россия"
                                        }
                                      ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                        {
                          "timestamp": "2025-11-12T16:30:12.345",
                          "statusCode": 401,
                          "error": "Unauthorized",
                          "body": "Требуется аутентификация",
                          "path": "/api/v1/users/me"
                        }
                        """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:15:12.345",
                                      "statusCode": 404,
                                      "error": "Not Found",
                                      "body": "Пользователь не найден",
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:16:12.345",
                                      "statusCode": 500,
                                      "error": "Internal Server Error",
                                      "body": "Внутренняя ошибка сервера",
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            )
    })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Обновить профиль текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "email": "newuser@example.com",
                                      "fullName": "Иван Иванов",
                                      "createdAt": "2025-11-12T16:00:12.345",
                                      "updatedAt": "2025-11-12T16:20:12.345",
                                      "roles": [{"id": 1, "name": "USER"}],
                                      "addresses": [
                                        {
                                          "id": 10,
                                          "street": "ул. Ленина, д.1",
                                          "city": "Москва",
                                          "zip": "101000",
                                          "state": "Москва",
                                          "country": "Россия"
                                        }
                                      ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса (валидация)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:25:12.345",
                                      "statusCode": 400,
                                      "error": "Bad Request",
                                      "body": {
                                        "errors": [
                                          { "field": "email", "messages": ["Email не может быть пустым", "Email должен быть корректным"] },
                                          { "field": "fullName", "messages": ["Полное имя не может быть пустым", "Имя должно быть от 2 до 100 символов"] },
                                          { "field": "addresses[0].street", "messages": ["Улица не может быть пустой"] }
                                        ]
                                      },
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                        {
                          "timestamp": "2025-11-12T16:30:12.345",
                          "statusCode": 401,
                          "error": "Unauthorized",
                          "body": "Требуется аутентификация",
                          "path": "/api/v1/users/me"
                        }
                        """)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:26:12.345",
                                      "statusCode": 404,
                                      "error": "Not Found",
                                      "body": "Пользователь не найден",
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Email уже занят",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:27:12.345",
                                      "statusCode": 409,
                                      "error": "Conflict",
                                      "body": "Пользователь с email already@example.com уже существует",
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-11-12T16:28:12.345",
                                      "statusCode": 500,
                                      "error": "Internal Server Error",
                                      "body": "Внутренняя ошибка сервера",
                                      "path": "/api/v1/users/me"
                                    }
                                    """)
                    )
            )
    })
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserResponse updatedProfile = userService.updateCurrentUserProfile(userUpdateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
