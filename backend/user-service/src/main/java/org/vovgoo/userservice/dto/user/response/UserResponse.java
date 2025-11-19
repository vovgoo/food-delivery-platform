package org.vovgoo.userservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.vovgoo.user.enums.UserStatus;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.dto.role.response.RoleResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Response containing user information")
public record UserResponse(

        @Schema(description = "Unique user ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "User phone number", example = "+375291234567")
        String phone,

        @Schema(description = "Full name of the user", example = "Иван Иванов")
        String fullName,

        @Schema(description = "Birth date of the user", example = "1990-05-21")
        LocalDate birthDate,

        @Schema(description = "Status of the user account", example = "ACTIVE")
        UserStatus userStatus,

        @Schema(description = "Date and time when the user was created", example = "2025-11-18T12:34:56")
        LocalDateTime createdAt,

        @Schema(description = "Date and time when the user was last updated", example = "2025-11-18T12:34:56")
        LocalDateTime updatedAt,

        @Schema(description = "Default address of the user")
        AddressResponse defaultAddress,

        @Schema(description = "Set of roles assigned to the user")
        Set<RoleResponse> roles
) {}
