package org.vovgoo.userservice.dto.security.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.vovgoo.userservice.validators.age.min.MinAge;
import org.vovgoo.validators.phone.Phone;

import java.time.LocalDate;

@Builder
@Schema(description = "Request for user sign-up")
public record SignUpRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        @Schema(description = "User phone number", example = "+375291234567")
        String phone,

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Полное имя должно быть от 2 до 100 символов")
        @Schema(description = "Full name of the user", example = "Иван Иванов")
        String fullName,

        @NotNull(message = "День рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        @MinAge(value = 16, message = "Пользователь должен быть старше 16 лет")
        @Schema(description = "Birth date (must be in the past)", example = "1990-05-21")
        LocalDate birthDate,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Пароль должен содержать латинские буквы, цифры и спецсимволы"
        )
        @Schema(description = "Password with letters, numbers and special characters", example = "P@ssw0rd123")
        String password
) {}
