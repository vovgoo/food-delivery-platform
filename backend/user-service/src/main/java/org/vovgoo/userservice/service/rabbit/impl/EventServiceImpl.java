package org.vovgoo.userservice.service.rabbit.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.domain.rabbit.event.EmailChangeEvent;
import org.vovgoo.domain.rabbit.event.PhoneChangeEvent;
import org.vovgoo.domain.rabbit.event.SignUpPhoneEvent;
import org.vovgoo.userservice.domain.rabbit.key.EventKeys;
import org.vovgoo.userservice.service.rabbit.EventService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventPublisherImpl eventPublisherImpl;

    public void publishPhoneChangeEvent(String phone, String otp) {
        PhoneChangeEvent event = PhoneChangeEvent.builder()
                .phone(phone)
                .otp(otp)
                .build();
        eventPublisherImpl.publish(EventKeys.PHONE_CHANGE, event);
    }

    public void publishEmailChangeEvent(String email, UUID token) {
        EmailChangeEvent event = EmailChangeEvent.builder()
                .email(email)
                .token(token)
                .build();
        eventPublisherImpl.publish(EventKeys.EMAIL_CHANGE, event);
    }

    public void publishSignUpEvent(String phone, String otp) {
        SignUpPhoneEvent event = SignUpPhoneEvent.builder()
                .phone(phone)
                .otp(otp)
                .build();
        eventPublisherImpl.publish(EventKeys.SIGNUP_PHONE, event);
    }
}
