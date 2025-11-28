package org.vovgoo.userservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.vovgoo.common.client.validation.email.domain.AllowedEmailDomain;

@Schema(description = "Request to change user's email")
public record ChangeEmailRequest(

        @AllowedEmailDomain
        @Email(message = "Почта должна быть корректной")
        @Size(max = 255, message = "Почта слишком длинная")
        @Schema(description = "New email address (must be valid and allowed domain)", example = "user@example.com")
        String email
) {}
