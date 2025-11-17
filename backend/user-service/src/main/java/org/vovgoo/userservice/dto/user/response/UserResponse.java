package org.vovgoo.userservice.dto.user.response;

import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.dto.role.response.RoleResponse;
import org.vovgoo.userservice.entity.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    String phone,
    String fullName,
    LocalDate birthDate,
    UserStatus userStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    AddressResponse defaultAddress,
    Set<RoleResponse> roles
) {}
