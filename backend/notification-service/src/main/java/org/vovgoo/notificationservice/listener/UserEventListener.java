package org.vovgoo.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.vovgoo.domain.rabbit.event.EmailChangeEvent;
import org.vovgoo.domain.rabbit.event.PhoneChangeEvent;
import org.vovgoo.domain.rabbit.event.SignUpPhoneEvent;
import org.vovgoo.notificationservice.config.frontend.FrontendProperty;
import org.vovgoo.notificationservice.service.sender.email.EmailSender;
import org.vovgoo.notificationservice.service.sender.sms.SmsSender;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final SmsSender smsSender;
    private final EmailSender emailSender;
    private final FrontendProperty frontendProperty;

    @RabbitListener(queues = "signup.phone.queue")
    public void handleSignupPhone(SignUpPhoneEvent event) {
        smsSender.send(event.phone(), String.format("Ваш код подтверждения регистрации: %s", event.otp()));
    }

    @RabbitListener(queues = "phone.change.queue")
    public void handlePhoneChange(PhoneChangeEvent event) {
        smsSender.send(event.phone(), String.format("Ваш код смены пароля: %s", event.otp()));
    }

    @RabbitListener(queues = "email.change.queue")
    public void handleEmailChange(EmailChangeEvent event) {
        emailSender.send(event.email(), "Смена почты", String.format("Для смены почты перейдите по ссылке: %s%s", frontendProperty.getEmailChangeRoute(), event.token()));
    }
}
