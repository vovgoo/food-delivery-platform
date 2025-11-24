package org.vovgoo.userservice.domain.redis.phone.change;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;
import java.util.UUID;

public class PhoneChangeAttemptsKey implements RedisKey<Integer> {
    private final UUID userId;
    private PhoneChangeAttemptsKey(UUID userId){ this.userId = userId; }
    public static PhoneChangeAttemptsKey of(UUID userId){ return new PhoneChangeAttemptsKey(userId); }
    public String asString(){ return "phone:change:attempts:" + userId; }
    public Class<Integer> valueType(){ return Integer.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
