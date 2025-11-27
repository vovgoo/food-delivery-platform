package org.vovgoo.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.vovgoo.common.event.user.rabbit.UserRabbitEventKeys;
import org.vovgoo.common.event.user.rabbit.event.EmailChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.PhoneChangeEvent;
import org.vovgoo.common.event.user.rabbit.event.SignUpPhoneEvent;
import org.vovgoo.notificationservice.config.frontend.FrontendProperty;
import org.vovgoo.notificationservice.service.sender.email.EmailSender;
import org.vovgoo.notificationservice.service.sender.sms.SmsSender;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final SmsSender smsSender;
    private final EmailSender emailSender;
    private final FrontendProperty frontendProperty;

    @RabbitListener(queues = UserRabbitEventKeys.SIGNUP_PHONE_QUEUE)
    public void handleSignupPhone(SignUpPhoneEvent event) {
        smsSender.send(event.phone(), String.format("Ваш код подтверждения регистрации: %s", event.otp()));
    }

    @RabbitListener(queues = UserRabbitEventKeys.PHONE_CHANGE_QUEUE)
    public void handlePhoneChange(PhoneChangeEvent event) {
        smsSender.send(event.phone(), String.format("Ваш код смены пароля: %s", event.otp()));
    }

    @RabbitListener(queues = UserRabbitEventKeys.EMAIL_CHANGE_QUEUE)
    public void handleEmailChange(EmailChangeEvent event) {
        emailSender.send(event.email(), "Смена почты", String.format("Для смены почты перейдите по ссылке: %s%s", frontendProperty.getEmailChangeRoute(), event.token()));
    }
}
