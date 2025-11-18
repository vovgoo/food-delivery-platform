package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.vovgoo.validators.phone.Phone;

@Schema(description = "Request to change user's phone number")
public record ChangePhoneRequest(

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        @Schema(description = "New phone number", example = "+375291234567")
        String phone
) {}
