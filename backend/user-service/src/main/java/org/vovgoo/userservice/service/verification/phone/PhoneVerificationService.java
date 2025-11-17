package org.vovgoo.userservice.service.verification.phone;

import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

public interface PhoneVerificationService {
    String sendOtp(String phone, PhoneVerificationType type);
    void validateOtp(String phoneHash, String code, PhoneVerificationType type);
}
