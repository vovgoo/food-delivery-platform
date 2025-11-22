package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.vovgoo.validators.phone.Phone;

@Schema(description = "Request to confirm phone number change with verification code")
public record ConfirmChangePhoneRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        @Schema(description = "User phone number", example = "+375291234567")
        String phone,

        @NotBlank(message = "Код не может быть пустым")
        @Pattern(regexp = "\\d{6}", message = "Код должен состоять ровно из 6 цифр")
        @Schema(description = "6-digit verification code sent to the user", example = "123456")
        String code
) {}
