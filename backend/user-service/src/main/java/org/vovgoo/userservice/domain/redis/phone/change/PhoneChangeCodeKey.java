package org.vovgoo.userservice.domain.redis.phone.change;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;
import java.util.UUID;

public class PhoneChangeCodeKey implements RedisKey<String> {
    private final UUID userId;
    private PhoneChangeCodeKey(UUID userId){ this.userId = userId; }
    public static PhoneChangeCodeKey of(UUID userId){ return new PhoneChangeCodeKey(userId); }
    public String asString(){ return "phone:change:code:" + userId; }
    public Class<String> valueType(){ return String.class; }
    public Duration ttl(){ return Duration.ofMinutes(10); }
}
