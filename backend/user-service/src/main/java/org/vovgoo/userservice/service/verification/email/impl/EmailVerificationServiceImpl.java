package org.vovgoo.userservice.service.verification.email.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.property.verification.VerificationProperty;
import org.vovgoo.userservice.exception.custom.verification.EmailVerificationAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.EmailVerificationNotFoundException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;
import org.vovgoo.userservice.service.rabbit.event.EmailVerificationEvent;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.verification.email.EmailVerificationService;
import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final RedisService redisService;
    private final VerificationProperty verificationProperty;
    private final EventPublisher eventPublisher;

    @Override
    public String sendVerificationLink(String email, EmailVerificationType type) {
        String token = UUID.randomUUID().toString();

        redisService.set(RedisKey.EMAIL_VERIFICATION_LINK, email, type.name(), token);

        redisService.set(RedisKey.EMAIL_VERIFICATION_ATTEMPTS, 0, type.name(), email);

        EmailVerificationEvent event = EmailVerificationEvent.builder()
                .email(email)
                .token(token)
                .emailVerificationType(type)
                .build();

        eventPublisher.publish(RabbitExchange.USER_EVENTS, RabbitRoutingKey.EMAIL_VERIFICATION_REQUESTED, event);

        return token;
    }

    @Override
    public void validateVerificationLink(String token, EmailVerificationType type) {
        String email = redisService.get(RedisKey.EMAIL_VERIFICATION_LINK, String.class, type.name(), token)
                .orElseThrow(EmailVerificationNotFoundException::new);

        Integer attempts = redisService.get(RedisKey.EMAIL_VERIFICATION_ATTEMPTS, Integer.class, type.name(), email)
                .orElse(0);

        if (attempts >= verificationProperty.getEmail().getMaxAttempts()) {
            redisService.delete(RedisKey.EMAIL_VERIFICATION_LINK, type.name(), token);
            redisService.delete(RedisKey.EMAIL_VERIFICATION_ATTEMPTS, type.name(), email);

            throw new EmailVerificationAttemptsExceededException();
        }

        redisService.delete(RedisKey.EMAIL_VERIFICATION_LINK, type.name(), token);
        redisService.delete(RedisKey.EMAIL_VERIFICATION_ATTEMPTS, type.name(), email);
    }
}
