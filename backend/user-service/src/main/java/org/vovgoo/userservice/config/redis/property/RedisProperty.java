package org.vovgoo.userservice.config.redis.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.redis.enums.RedisKey;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.redis")
public class RedisProperty {

    private Map<String, RedisKeyConfig> keys = new HashMap<>();

    @Data
    public static class RedisKeyConfig {
        private String key;
        private Long ttlMs;
    }

    public RedisKeyConfig getKeyConfig(RedisKey redisKey) {
        return keys.get(redisKey.getKey());
    }
}
