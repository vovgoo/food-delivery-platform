package org.vovgoo.userservice.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        String oldPassword,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        String newPassword
) {}
