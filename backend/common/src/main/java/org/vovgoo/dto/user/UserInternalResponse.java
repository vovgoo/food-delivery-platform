package org.vovgoo.dto.user;

import org.vovgoo.enums.user.UserStatus;

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
