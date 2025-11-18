package org.vovgoo.userservice.service.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.rabbitmq.property.RabbitProperty;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventSerializationException;
import org.vovgoo.userservice.exception.custom.messaging.RabbitEventTypeMismatchException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitProperty rabbitProperty;
    private ObjectMapper objectMapper;
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        rabbitProperty = mock(RabbitProperty.class);
        objectMapper = mock(ObjectMapper.class);

        eventPublisher = new EventPublisher(rabbitTemplate, rabbitProperty, objectMapper);
    }

    static class DummyEvent {}

    @Test
    void publish_shouldSendMessage_whenPayloadTypeMatches() throws Exception {
        RabbitRoutingKey key = RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED;
        RabbitExchange exchange = RabbitExchange.USER_EVENTS;

        var payload = new org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent(
                PhoneVerificationType.CHANGE, "+375291231231", "123123");

        when(objectMapper.writeValueAsString(payload)).thenReturn("{\"dummy\":\"value\"}");
        when(rabbitProperty.getExchangeName(exchange)).thenReturn("exchange-name");

        eventPublisher.publish(exchange, key, payload);

        verify(rabbitTemplate).convertAndSend(eq("exchange-name"), eq(key.getName()), eq("{\"dummy\":\"value\"}"));
    }

    @Test
    void publish_shouldThrowTypeMismatchException_whenPayloadTypeMismatch() {
        RabbitRoutingKey key = RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED;
        RabbitExchange exchange = RabbitExchange.USER_EVENTS;

        DummyEvent payload = new DummyEvent();

        assertThrows(RabbitEventTypeMismatchException.class,
                () -> eventPublisher.publish(exchange, key, payload));

        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void publish_shouldThrowSerializationException_whenJsonFails() throws Exception {
        RabbitRoutingKey key = RabbitRoutingKey.PHONE_VERIFICATION_REQUESTED;
        RabbitExchange exchange = RabbitExchange.USER_EVENTS;

        org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent payload =
                new org.vovgoo.userservice.service.rabbit.event.PhoneVerificationEvent(PhoneVerificationType.CHANGE, "+375291231231", "123123");

        when(objectMapper.writeValueAsString(payload)).thenThrow(JsonProcessingException.class);
        when(rabbitProperty.getExchangeName(exchange)).thenReturn("exchange-name");

        assertThrows(RabbitEventSerializationException.class,
                () -> eventPublisher.publish(exchange, key, payload));

        verifyNoInteractions(rabbitTemplate);
    }
}