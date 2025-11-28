package org.vovgoo.common.domain.user.dto;

import org.vovgoo.common.domain.user.enums.UserStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UserInternalResponse(
        UUID id,
        String email,
        String phone,
        String fullName,
        LocalDate birthDate,
        UserStatus userStatus
) {}
