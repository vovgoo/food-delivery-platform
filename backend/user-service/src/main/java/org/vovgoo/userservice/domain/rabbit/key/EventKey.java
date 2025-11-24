package org.vovgoo.userservice.domain.rabbit.key;

import java.time.Duration;

public class EventKey<T> {

    private final String routingKey;
    private final Class<T> payloadType;
    private final Duration ttl;

    public EventKey(String routingKey, Class<T> payloadType, Duration ttl) {
        this.routingKey = routingKey;
        this.payloadType = payloadType;
        this.ttl = ttl;
    }

    public String key() { return routingKey; }
    public Class<T> payloadType() { return payloadType; }
    public Duration ttl() { return ttl; }
}
