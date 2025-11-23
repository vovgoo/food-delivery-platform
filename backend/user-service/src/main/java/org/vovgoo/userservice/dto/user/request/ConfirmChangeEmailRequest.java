package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request to confirm email with verification token")
public record ConfirmChangeEmailRequest(

        @NotNull(message = "Token не может быть пустым")
        @Schema(description = "Verification token sent to the user's email", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID token
) {}
