package org.vovgoo.userservice.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.domain.redis.RedisKey;
import org.vovgoo.userservice.exception.custom.messaging.RedisSerializationException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void set(RedisKey<T> key, T value) {
        redisTemplate.opsForValue().set(key.asString(), value, key.ttl());
    }

    public <T> Optional<T> get(RedisKey<T> key) {
        Object value = redisTemplate.opsForValue().get(key.asString());
        if (value == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.convertValue(value, key.valueType()));
        } catch (IllegalArgumentException e) {
            throw new RedisSerializationException("Failed to deserialize value for key: " + key.asString(), e);
        }
    }

    public <T> void delete(RedisKey<T> key) {
        redisTemplate.delete(key.asString());
    }
}
