package org.vovgoo.userservice.service.verification.email;

import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

public interface EmailVerificationService {
    String send(String email, EmailVerificationType type);
    void validate(String token, EmailVerificationType type);
}
