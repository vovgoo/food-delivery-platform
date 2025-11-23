package org.vovgoo.userservice.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.domain.redis.RedisKey;

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
        return Optional.of(objectMapper.convertValue(value, key.valueType()));
    }

    public <T> void delete(RedisKey<T> key) {
        redisTemplate.delete(key.asString());
    }
}
