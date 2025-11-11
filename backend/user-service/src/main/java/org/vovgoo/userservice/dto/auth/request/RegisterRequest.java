package org.vovgoo.userservice.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        String password,

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String fullName
) {}
