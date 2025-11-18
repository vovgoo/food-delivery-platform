package org.vovgoo.userservice.dto.security.auth.request;

import jakarta.validation.constraints.*;
import org.vovgoo.userservice.validators.phone.Phone;

import java.time.LocalDate;

public record SignUpRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        String phone,

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Полное имя должно быть от 2 до 100 символов")
        String fullName,

        @NotNull(message = "День рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        String password
) {}
