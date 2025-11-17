package org.vovgoo.userservice.config.rabbitmq.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitQueue;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitProperty {

    private Map<String, ExchangeConfig> exchanges = new HashMap<>();
    private Map<String, QueueConfig> queues = new HashMap<>();

    @Data
    public static class ExchangeConfig {
        private String name;
    }

    @Data
    public static class QueueConfig {
        private String name;
    }

    public String getExchangeName(RabbitExchange exchange) {
        ExchangeConfig config = exchanges.get(exchange.getName());
        if (config == null) {
            throw new IllegalArgumentException("Exchange not found: " + exchange.getName());
        }
        return config.getName();
    }

    public String getQueueName(RabbitQueue queue) {
        QueueConfig config = queues.get(queue.getName());
        if (config == null) {
            throw new IllegalArgumentException("Queue not found: " + queue.getName());
        }
        return config.getName();
    }
}
