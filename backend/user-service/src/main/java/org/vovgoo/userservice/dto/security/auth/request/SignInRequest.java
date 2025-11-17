package org.vovgoo.userservice.dto.security.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.vovgoo.userservice.validators.phone.Phone;

@Schema(description = "Запрос для авторизации пользователя")
public record SignInRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        String phone,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        String password
) {}
