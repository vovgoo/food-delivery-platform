package org.vovgoo.orderservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.common.domain.user.enums.UserStatus;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Response object representing a user")
public record UserResponse(

        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "User phone number", example = "+7 (495) 123-45-67")
        String phone,

        @Schema(description = "Full name of the user", example = "Иван Иванов")
        String fullName,

        @Schema(description = "Birth date of the user", example = "1990-01-01")
        LocalDate birthDate,

        @Schema(description = "Current status of the user", example = "ACTIVE")
        UserStatus userStatus
) {}
