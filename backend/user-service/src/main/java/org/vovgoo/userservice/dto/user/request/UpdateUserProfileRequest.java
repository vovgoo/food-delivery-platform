package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.vovgoo.userservice.validators.age.min.MinAge;

import java.time.LocalDate;

@Schema(description = "Request to update user's profile information")
public record UpdateUserProfileRequest(

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Полное имя должно быть от 2 до 100 символов")
        @Schema(description = "Full name of the user", example = "Иван Иванов")
        String fullName,

        @NotNull(message = "День рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        @MinAge(value = 16, message = "Пользователь должен быть старше 16 лет")
        @Schema(description = "Birth date (must be in the past)", example = "1990-05-21")
        LocalDate birthDate
) {}
