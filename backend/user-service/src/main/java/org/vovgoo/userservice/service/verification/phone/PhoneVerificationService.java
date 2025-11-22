package org.vovgoo.userservice.service.verification.phone;

import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

public interface PhoneVerificationService {
    void send(String phone, PhoneVerificationType type);
    void validate(String phone, String code, PhoneVerificationType type);
}
