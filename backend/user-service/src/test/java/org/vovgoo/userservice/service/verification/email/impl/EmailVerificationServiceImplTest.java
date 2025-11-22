package org.vovgoo.userservice.service.verification.email.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.security.property.verification.VerificationProperty;
import org.vovgoo.userservice.exception.custom.verification.EmailVerificationAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.EmailVerificationNotFoundException;
import org.vovgoo.userservice.service.rabbit.EventPublisher;
import org.vovgoo.userservice.service.rabbit.event.EmailVerificationEvent;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.verification.email.enums.EmailVerificationType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailVerificationServiceImplTest {

    private RedisService redisService;
    private VerificationProperty verificationProperty;
    private EventPublisher eventPublisher;
    private EmailVerificationServiceImpl emailVerificationService;

    @BeforeEach
    void setUp() {
        redisService = mock(RedisService.class);
        verificationProperty = mock(VerificationProperty.class);
        eventPublisher = mock(EventPublisher.class);

        var emailProps = mock(VerificationProperty.Email.class);
        when(verificationProperty.getEmail()).thenReturn(emailProps);
        when(emailProps.getMaxAttempts()).thenReturn(3);

        emailVerificationService = new EmailVerificationServiceImpl(redisService, verificationProperty, eventPublisher);
    }

    @Test
    void sendVerificationLink_shouldStoreAndPublishEvent() {
        String email = "test@example.com";
        EmailVerificationType type = EmailVerificationType.CHANGE;

        String token = emailVerificationService.send(email, type);

        assertNotNull(token);

        verify(redisService).set(any(), eq(email), eq(type.name()), eq(token));
        verify(redisService).set(any(), eq(0), eq(type.name()), eq(email));

        verify(eventPublisher).publish(
                eq(RabbitExchange.USER_EVENTS),
                eq(RabbitRoutingKey.EMAIL_VERIFICATION_REQUESTED),
                any(EmailVerificationEvent.class)
        );
    }

    @Test
    void validateVerificationLink_shouldThrowNotFoundIfMissing() {
        String token = "token123";
        EmailVerificationType type = EmailVerificationType.CHANGE;

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.empty());

        assertThrows(EmailVerificationNotFoundException.class,
                () -> emailVerificationService.validate(token, type));
    }

    @Test
    void validateVerificationLink_shouldThrowAttemptsExceeded() {
        String token = "token123";
        EmailVerificationType type = EmailVerificationType.CHANGE;
        String email = "test@example.com";

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of(email));
        when(redisService.get(any(), eq(Integer.class), eq(type.name()), eq(email)))
                .thenReturn(Optional.of(3));

        assertThrows(EmailVerificationAttemptsExceededException.class,
                () -> emailVerificationService.validate(token, type));

        verify(redisService).delete(any(), eq(type.name()), eq(token));
        verify(redisService).delete(any(), eq(type.name()), eq(email));
    }

    @Test
    void validateVerificationLink_shouldSucceedAndDeleteKeys() {
        String token = "token123";
        EmailVerificationType type = EmailVerificationType.CHANGE;
        String email = "test@example.com";

        when(redisService.get(any(), eq(String.class), eq(type.name()), eq(token)))
                .thenReturn(Optional.of(email));
        when(redisService.get(any(), eq(Integer.class), eq(type.name()), eq(email)))
                .thenReturn(Optional.of(1));

        assertDoesNotThrow(() -> emailVerificationService.validate(token, type));

        verify(redisService).delete(any(), eq(type.name()), eq(token));
        verify(redisService).delete(any(), eq(type.name()), eq(email));
    }
}
