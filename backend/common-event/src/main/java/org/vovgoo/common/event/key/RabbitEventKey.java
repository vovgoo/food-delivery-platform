package org.vovgoo.common.event.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class RabbitEventKey<T> {
    private final String exchange;
    private final String queueName;
    private final String routingKey;
    private final Class<T> payloadType;
    private final Duration ttl;
}
