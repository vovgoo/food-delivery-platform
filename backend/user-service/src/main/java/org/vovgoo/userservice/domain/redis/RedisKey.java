package org.vovgoo.userservice.domain.redis;

import java.time.Duration;

public interface RedisKey<T> {
    String asString();
    Class<T> valueType();
    Duration ttl();
}
