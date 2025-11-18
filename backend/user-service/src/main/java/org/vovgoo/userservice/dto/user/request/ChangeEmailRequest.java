package org.vovgoo.userservice.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.vovgoo.userservice.validators.email.domain.AllowedEmailDomain;

public record ChangeEmailRequest(

        @AllowedEmailDomain
        @Email(message = "Почта должна быть корректной")
        @Size(max = 255, message = "Почта слишком длинная")
        String email
) {}
