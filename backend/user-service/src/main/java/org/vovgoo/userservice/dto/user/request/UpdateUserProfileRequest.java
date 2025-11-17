package org.vovgoo.userservice.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserProfileRequest(

        @NotBlank(message = "Полное имя не может быть пустым")
        @Size(min = 2, max = 100, message = "Полное имя должно быть от 2 до 100 символов")
        String fullName,

        @NotNull(message = "День рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate
) {}
