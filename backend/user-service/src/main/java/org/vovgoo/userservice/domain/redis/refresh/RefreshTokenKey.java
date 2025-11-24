package org.vovgoo.userservice.domain.redis.refresh;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;
import java.util.UUID;

public class RefreshTokenKey implements RedisKey<String> {
    private final UUID userId;
    private RefreshTokenKey(UUID userId) { this.userId = userId; }
    public static RefreshTokenKey of(UUID userId) { return new RefreshTokenKey(userId); }
    public String asString() { return "refresh:token:" + userId; }
    public Class<String> valueType() { return String.class; }
    public Duration ttl() { return Duration.ofDays(15); }
}
