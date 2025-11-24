package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to confirm a user's phone change using a verification code")
public record ConfirmChangePhoneRequest(

        @NotBlank(message = "Код не может быть пустым")
        @Pattern(regexp = "\\d{6}", message = "Код должен состоять ровно из 6 цифр")
        @Schema(description = "6-digit verification code sent to the user", example = "123456")
        String code
) {}
