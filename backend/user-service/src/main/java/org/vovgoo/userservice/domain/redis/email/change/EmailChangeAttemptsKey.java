package org.vovgoo.userservice.domain.redis.email.change;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;
import java.util.UUID;

public class EmailChangeAttemptsKey implements RedisKey<Integer> {
    private final UUID userId;
    private EmailChangeAttemptsKey(UUID userId){ this.userId = userId; }
    public static EmailChangeAttemptsKey of(UUID userId){ return new EmailChangeAttemptsKey(userId); }
    public String asString(){ return "email:change:attempts:" + userId; }
    public Class<Integer> valueType(){ return Integer.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
