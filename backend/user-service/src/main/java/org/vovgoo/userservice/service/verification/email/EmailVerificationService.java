package org.vovgoo.userservice.service.verification.email;

import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

import java.util.UUID;

public interface EmailVerificationService {
    String send(String email, EmailVerificationType type);
    void validate(UUID token, EmailVerificationType type);
}
