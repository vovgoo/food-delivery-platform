package org.vovgoo.userservice.service.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.rabbitmq.property.RabbitProperty;
import org.vovgoo.userservice.exception.custom.RabbitEventSerializationException;
import org.vovgoo.userservice.exception.custom.RabbitEventTypeMismatchException;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperty rabbitProperty;
    private final ObjectMapper objectMapper;

    public <T> void publish(RabbitExchange exchange, RabbitRoutingKey routingKey, T payload) {
        if (!routingKey.getEventType().isAssignableFrom(payload.getClass())) {
            throw new RabbitEventTypeMismatchException("Неверный тип события для данного routing key");
        }

        try {
            String json = objectMapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend(rabbitProperty.getExchangeName(exchange), rabbitProperty.getRoutingKey(routingKey), json);
        } catch (JsonProcessingException e) {
            throw new RabbitEventSerializationException("Ошибка сериализации события для RabbitMQ", e);
        }
    }
}
