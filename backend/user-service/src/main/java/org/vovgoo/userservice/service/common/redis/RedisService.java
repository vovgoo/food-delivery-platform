package org.vovgoo.userservice.service.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.redis.enums.RedisKey;
import org.vovgoo.userservice.config.redis.property.RedisProperty;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisProperty redisProperty;
    private final ObjectMapper objectMapper;

    public <T> void set(RedisKey redisKey, String id, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            String key = formatKey(redisKey, id);
            Duration ttl = getTtl(redisKey);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации объекта для Redis", e);
        }
    }

    public <T> Optional<T> get(RedisKey redisKey, String id, Class<T> clazz) {
        String key = formatKey(redisKey, id);
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка десериализации объекта из Redis", e);
        }
    }

    public void delete(RedisKey redisKey, String id) {
        String key = formatKey(redisKey, id);
        redisTemplate.delete(key);
    }

    private String formatKey(RedisKey redisKey, String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID для RedisKey не может быть пустым");
        }

        RedisProperty.RedisKeyConfig config = redisProperty.getKeyConfig(redisKey);
        if (config == null || config.getKey() == null || config.getKey().isBlank()) {
            throw new IllegalStateException("Ключ для " + redisKey.getKey() + " не задан в конфигурации");
        }

        return config.getKey() + ":" + id;
    }

    private Duration getTtl(RedisKey redisKey) {
        RedisProperty.RedisKeyConfig config = redisProperty.getKeyConfig(redisKey);
        if (config == null || config.getTtlMs() == null) {
            throw new IllegalStateException("TTL для ключа " + redisKey.getKey() + " не задан в конфигурации");
        }
        return Duration.ofMillis(config.getTtlMs());
    }
}
