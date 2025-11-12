package org.vovgoo.userservice.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос для регистрации нового пользователя")
public record RegisterRequest(

        @Schema(description = "Email нового пользователя", example = "newuser@example.com")
        @Email(message = "Email должен быть корректным")
        @NotBlank(message = "Email не может быть пустым")
        @Size(max = 255, message = "Email слишком длинный")
        String email,

        @Schema(description = "Пароль пользователя (латинские буквы, цифры, спецсимволы)", example = "Passw0rd!")
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        String password,

        @Schema(description = "Полное имя пользователя", example = "Иван Иванов")
        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
        String fullName
) {}
