package org.vovgoo.userservice.dto.user.response;

import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.dto.role.response.RoleResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<AddressResponse> addresses,
        Set<RoleResponse> roles
) {}
