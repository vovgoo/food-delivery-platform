package org.vovgoo.userservice.domain.redis.email.change;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;
import java.util.UUID;

public class EmailChangeTokenKey implements RedisKey<UUID> {
    private final UUID userId;
    private EmailChangeTokenKey(UUID userId){ this.userId = userId; }
    public static EmailChangeTokenKey of(UUID userId){ return new EmailChangeTokenKey(userId); }
    public String asString(){ return "email:change:token:" + userId; }
    public Class<UUID> valueType(){ return UUID.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
