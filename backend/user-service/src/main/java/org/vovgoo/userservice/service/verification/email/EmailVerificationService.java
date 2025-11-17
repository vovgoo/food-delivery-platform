package org.vovgoo.userservice.service.verification.email;

import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

public interface EmailVerificationService {
    String sendVerificationLink(String email, EmailVerificationType type);
    void validateVerificationLink(String token, EmailVerificationType type);
}
