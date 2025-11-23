package org.vovgoo.userservice.service.rabbit;

import java.util.UUID;

public interface EventService {
    void publishPhoneChangeEvent(String phone, String otp);
    void publishEmailChangeEvent(String email, UUID token);
    void publishSignUpEvent(String phone, String otp);
}
