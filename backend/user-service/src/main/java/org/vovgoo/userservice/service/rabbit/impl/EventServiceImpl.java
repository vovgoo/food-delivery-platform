package org.vovgoo.userservice.service.rabbit.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.common.event.user.rabbit.UserRabbitEventKeys;
import org.vovgoo.common.event.user.rabbit.event.EmailChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.PhoneChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.SignUpPhoneEvent;
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
        eventPublisherImpl.publish(UserRabbitEventKeys.PHONE_CHANGE, event);
    }

    public void publishEmailChangeEvent(String email, UUID token) {
        EmailChangeEvent event = EmailChangeEvent.builder()
                .email(email)
                .token(token)
                .build();
        eventPublisherImpl.publish(UserRabbitEventKeys.EMAIL_CHANGE, event);
    }

    public void publishSignUpEvent(String phone, String otp) {
        SignUpPhoneEvent event = SignUpPhoneEvent.builder()
                .phone(phone)
                .otp(otp)
                .build();
        eventPublisherImpl.publish(UserRabbitEventKeys.SIGNUP_PHONE, event);
    }
}
