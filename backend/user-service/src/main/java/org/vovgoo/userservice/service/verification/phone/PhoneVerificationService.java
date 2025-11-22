package org.vovgoo.userservice.service.verification.phone;

import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

public interface PhoneVerificationService {
    String send(String phone, PhoneVerificationType type);
    void validate(String phoneHash, String code, PhoneVerificationType type);
}
