package org.vovgoo.userservice.config.rabbitmq.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitExchange;
import org.vovgoo.userservice.config.rabbitmq.enums.RabbitRoutingKey;

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
        private String type;
    }

    @Data
    public static class QueueConfig {
        private String name;
        private Map<String, BindingConfig> bindings = new HashMap<>();
    }

    @Data
    public static class BindingConfig {
        private String exchange;
        private String routingKey;
    }

    public String getExchangeName(RabbitExchange exchange) {
        ExchangeConfig config = exchanges.get(exchange.getName());
        if (config == null) {
            throw new IllegalArgumentException("Exchange not found: " + exchange.getName());
        }
        return config.getName();
    }

    public String getRoutingKey(RabbitRoutingKey routingKey) {
        for (QueueConfig queueConfig : queues.values()) {
            BindingConfig binding = queueConfig.getBindings().get(routingKey.getName());
            if (binding != null) {
                return binding.getRoutingKey();
            }
        }
        throw new IllegalArgumentException("RoutingKey not found: " + routingKey.getName());
    }
}
