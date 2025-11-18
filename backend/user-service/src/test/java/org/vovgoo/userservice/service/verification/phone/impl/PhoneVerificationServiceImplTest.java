package org.vovgoo.userservice.service.verification.phone.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneVerificationServiceImplTest {

    private RedisService redisService;
    private VerificationProperty verificationProperty;
    private EventPublisher eventPublisher;
    private PhoneVerificationServiceImpl phoneVerificationService;

    @BeforeEach
    void setUp() {
        redisService = mock(RedisService.class);
        verificationProperty = mock(VerificationProperty.class);
        eventPublisher = mock(EventPublisher.class);

        var phoneProps = mock(VerificationProperty.Phone.class);
        when(verificationProperty.getPhone()).thenReturn(phoneProps);
        when(phoneProps.getMaxAttempts()).thenReturn(3);

        phoneVerificationService = new PhoneVerificationServiceImpl(redisService, verificationProperty, eventPublisher);
    }

    @Test
    void sendOtp_shouldStoreAndPublishEvent() {
        String phone = "+375291231231";
        PhoneVerificationType type = PhoneVerificationType.CHANGE;

        String token = phoneVerificationService.sendOtp(phone, type);

        assertNotNull(token);

        verify(redisService).set(any(), anyString(), eq(type.name()), anyString());
        verify(redisService).set(any(), eq(true), eq(type.name()), anyString());
        verify(redisService).set(any(), eq(0), eq(type.name()), anyString());

        verify(eventPublisher).publish(
                eq(RabbitExchange.USER_EVENTS),
                eq(RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED),
                any(PhoneVerificationEvent.class)
        );
    }

    @Test
    void validateOtp_shouldThrowNotFoundIfMissing() {
        String token = "token123";
        PhoneVerificationType type = PhoneVerificationType.CHANGE;

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.empty());

        assertThrows(OtpNotFoundException.class,
                () -> phoneVerificationService.validateOtp(token, "123456", type));
    }

    @Test
    void validateOtp_shouldThrowInvalidOtpAndIncrementAttempts() {
        String token = "token123";
        PhoneVerificationType type = PhoneVerificationType.CHANGE;

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of("654321"));
        when(redisService.get(any(), eq(Integer.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of(1));

        assertThrows(InvalidOtpException.class,
                () -> phoneVerificationService.validateOtp(token, "123456", type));

        verify(redisService).set(any(), eq(2), eq(type.name()), eq(token));
    }

    @Test
    void validateOtp_shouldSucceedAndDeleteKeys() {
        String token = "token123";
        PhoneVerificationType type = PhoneVerificationType.CHANGE;

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of("654321"));

        assertDoesNotThrow(() -> phoneVerificationService.validateOtp(token, "654321", type));

        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), token);
        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_RATE_LIMIT, type.name(), token);
        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), token);
    }

    @Test
    void validateOtp_shouldThrowAttemptsExceededAndDeleteKeys() {
        String token = "token123";
        PhoneVerificationType type = PhoneVerificationType.CHANGE;

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of("654321"));
        when(redisService.get(any(), eq(Integer.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of(3));

        assertThrows(OtpAttemptsExceededException.class,
                () -> phoneVerificationService.validateOtp(token, "123456", type));

        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_CODE, type.name(), token);
        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_RATE_LIMIT, type.name(), token);
        verify(redisService).delete(RedisKey.PHONE_VERIFICATION_ATTEMPTS, type.name(), token);
    }
}
