package org.vovgoo.userservice.service.verification.phone.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.property.verification.VerificationProperty;
import org.vovgoo.userservice.exception.custom.verification.InvalidOtpException;
import org.vovgoo.userservice.exception.custom.verification.OtpAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.OtpNotFoundException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;
import org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PhoneVerificationServiceImpl implements PhoneVerificationService {

    private final RedisService redisService;
    private final VerificationProperty verificationProperty;
    private final EventPublisher eventPublisher;

    @Override
    public void send(String phone, PhoneVerificationType type) {
        String otpCode = String.valueOf(ThreadLocalRandom.current().nextInt(100_000, 1_000_000));

        redisService.set(RedisKey.PHONE_VERIFICATION_CODE, otpCode, type.name(), phone);
        redisService.set(RedisKey.PHONE_VERIFICATION_ATTEMPTS, 0, type.name(), phone);

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
    }

    @Override
    public void validate(String phone, String code, PhoneVerificationType type) {
        String actualOtp = redisService.get(RedisKey.PHONE_VERIFICATION_CODE, String.class, type.name(), phone)
                .orElseThrow(OtpNotFoundException::new);

        Integer attempts = redisService.get(RedisKey.PHONE_VERIFICATION_ATTEMPTS, Integer.class, type.name(), phone)
                .orElse(0);

        byte[] expected = actualOtp.getBytes(StandardCharsets.UTF_8);
        byte[] provided = code.getBytes(StandardCharsets.UTF_8);

        boolean valid = expected.length == provided.length && MessageDigest.isEqual(expected, provided);

        if (!valid) {
            attempts++;
            redisService.set(RedisKey.PHONE_VERIFICATION_ATTEMPTS, attempts, type.name(), phone);

            if (attempts >= verificationProperty.getPhone().getMaxAttempts()) {
                redisService.delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), phone);
                redisService.delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), phone);

                throw new OtpAttemptsExceededException();
            }

            throw new InvalidOtpException();
        }

        redisService.delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), phone);
        redisService.delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), phone);
    }
}
