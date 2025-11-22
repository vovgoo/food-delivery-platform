package org.vovgoo.userservice.dto.security.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.vovgoo.validators.phone.Phone;

@Schema(description = "Request to confirm user sign-up with verification code")
public record ConfirmSignUpRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        @Schema(description = "User phone number", example = "+375291234567")
        String phone,

        @NotBlank(message = "Код не может быть пустым")
        @Pattern(regexp = "\\d{6}", message = "Код должен состоять ровно из 6 цифр")
        @Schema(description = "6-digit verification code sent to the user", example = "123456")
        String code
) {}
