package org.vovgoo.userservice.dto.security.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.vovgoo.common.domain.validation.phone.Phone;

@Schema(description = "Request for user sign-in")
public record SignInRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        @Schema(description = "User phone number", example = "+375291234567")
        String phone,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Schema(description = "User password", example = "P@ssw0rd123")
        String password
) {}
