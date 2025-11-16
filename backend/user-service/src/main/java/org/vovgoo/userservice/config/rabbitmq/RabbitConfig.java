package org.vovgoo.userservice.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.rabbitmq.property.RabbitProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperty rabbitProperty;

    @Bean
    public Declarables declarables() {
        List<Declarable> declarable = new ArrayList<>();
        Map<String, Exchange> exchangeMap = new HashMap<>();

        rabbitProperty.getExchanges().forEach((key, cfg) -> {
            Exchange ex = switch (cfg.getType().toLowerCase()) {
                case "topic" -> ExchangeBuilder.topicExchange(cfg.getName()).durable(true).build();
                case "fanout" -> ExchangeBuilder.fanoutExchange(cfg.getName()).durable(true).build();
                default -> ExchangeBuilder.directExchange(cfg.getName()).durable(true).build();
            };
            declarable.add(ex);
            exchangeMap.put(key, ex);
        });

        rabbitProperty.getQueues().forEach((qKey, qCfg) -> {
            Queue queue = QueueBuilder.durable(qCfg.getName()).build();
            declarable.add(queue);

            qCfg.getBindings().forEach((bKey, bCfg) -> {
                Exchange exchange = exchangeMap.get(bCfg.getExchange());
                if (exchange == null) {
                    throw new IllegalArgumentException(
                            "Binding '" + bKey + "' references unknown exchange '" + bCfg.getExchange() + "'"
                    );
                }

                Binding binding = switch (exchange) {
                    case DirectExchange e -> BindingBuilder.bind(queue).to(e).with(bCfg.getRoutingKey());
                    case TopicExchange e -> BindingBuilder.bind(queue).to(e).with(bCfg.getRoutingKey());
                    case FanoutExchange e -> BindingBuilder.bind(queue).to(e);
                    default -> throw new IllegalStateException("Unsupported exchange type");
                };

                declarable.add(binding);
            });
        });

        return new Declarables(declarable);
    }
}
