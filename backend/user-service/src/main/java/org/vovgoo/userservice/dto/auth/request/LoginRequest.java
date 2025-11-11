package org.vovgoo.userservice.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        String password
) {}
