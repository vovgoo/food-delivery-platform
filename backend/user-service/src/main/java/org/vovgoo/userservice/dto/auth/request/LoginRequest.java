package org.vovgoo.userservice.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для авторизации пользователя")
public record LoginRequest(

        @Schema(description = "Email пользователя", example = "user@example.com")
        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @Schema(description = "Пароль пользователя", example = "Passw0rd!")
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        String password
) {}
