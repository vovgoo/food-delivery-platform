package org.vovgoo.userservice.service.common.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;
import org.vovgoo.userservice.config.rabbitmq.property.RabbitProperty;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperty rabbitProperty;
    private final ObjectMapper objectMapper;

    public <T> void publish(RabbitExchange exchange, RabbitRoutingKey routingKey, T payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend(rabbitProperty.getExchangeName(exchange), rabbitProperty.getRoutingKey(routingKey), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации события для RabbitMQ", e);
        }
    }
}
