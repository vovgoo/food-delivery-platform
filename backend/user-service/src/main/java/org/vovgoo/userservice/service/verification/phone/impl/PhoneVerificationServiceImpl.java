package org.vovgoo.userservice.service.verification.phone.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.property.VerificationProperty;
import org.vovgoo.userservice.exception.custom.InvalidOtpException;
import org.vovgoo.userservice.exception.custom.OtpAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.OtpNotFoundException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;
import org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PhoneVerificationServiceImpl implements PhoneVerificationService {

    private final RedisService redisService;
    private final VerificationProperty verificationProperty;
    private final EventPublisher eventPublisher;

    @Override
    public String sendOtp(String phone, PhoneVerificationType type) {
        String token = UUID.randomUUID().toString();

        String otpCode = String.valueOf(ThreadLocalRandom.current().nextInt(100_000, 1_000_000));

        redisService.set(RedisKey.PHONE_VERIFICATION_CODE, otpCode, type.name(), token);

        redisService.set(RedisKey.PHONE_VERIFICATION_RATE_LIMIT, true, type.name(), token);

        PhoneVerificationEvent event = PhoneVerificationEvent.builder()
                .phone(phone)
                .code(otpCode)
                .phoneVerificationType(type)
                .build();

        eventPublisher.publish(
                RabbitExchange.USER_EVENTS,
                RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED,
                event
        );

        return token;
    }

    @Override
    public void validateOtp(String token, String code, PhoneVerificationType type) {
        String actualOtp = redisService.get(RedisKey.PHONE_VERIFICATION_CODE, String.class, type.name(), token)
                .orElseThrow(OtpNotFoundException::new);

        if (!actualOtp.equals(code)) {
            incrementAttemptsOrFail(token, type);
            throw new InvalidOtpException();
        }

        redisService.delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), token);
        redisService.delete(RedisKey.PHONE_VERIFICATION_RATE_LIMIT, type.name(), token);
        redisService.delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), token);
    }

    private void incrementAttemptsOrFail(String token, PhoneVerificationType type) {
        Integer attempts = redisService.get(RedisKey.PHONE_VERIFICATION_ATTEMPTS, Integer.class, type.name(), token)
                .orElse(0);
        attempts++;

        redisService.set(RedisKey.PHONE_VERIFICATION_ATTEMPTS, attempts, type.name(), token);

        if (attempts >= verificationProperty.getPhone().getMaxAttempts()) {
            redisService.delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), token);
            redisService.delete(RedisKey.PHONE_VERIFICATION_RATE_LIMIT, type.name(), token);
            redisService.delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), token);

            throw new OtpAttemptsExceededException();
        }
    }
}
