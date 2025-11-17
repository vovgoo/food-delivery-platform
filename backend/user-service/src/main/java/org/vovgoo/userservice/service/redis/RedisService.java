package org.vovgoo.userservice.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.exception.custom.RedisKeyTypeMismatchException;
import org.vovgoo.userservice.exception.custom.RedisSerializationException;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void set(RedisKey key, Object value, String... args) {
        if (!key.getValueType().isInstance(value)) {
            throw new RedisKeyTypeMismatchException(
                    "Несоответствие типа для ключа " + key.name() +
                            ". Ожидалось " + key.getValueType().getSimpleName() +
                            ", получили " + value.getClass().getSimpleName()
            );
        }

        try {
            String json = objectMapper.writeValueAsString(value);
            String redisKey = key.buildKey(args);
            redisTemplate.opsForValue().set(redisKey, json, key.getTtl());
        } catch (JsonProcessingException e) {
            throw new RedisSerializationException("Ошибка сериализации объекта для Redis", e);
        }
    }

    public <T> Optional<T> get(RedisKey key, Class<T> clazz, String... args) {
        if (!key.getValueType().equals(clazz)) {
            throw new RedisKeyTypeMismatchException(
                    "Несоответствие типа при чтении ключа " + key.name() +
                            ". Ожидалось " + key.getValueType().getSimpleName() +
                            ", запросили " + clazz.getSimpleName()
            );
        }

        String redisKey = key.buildKey(args);
        String json = redisTemplate.opsForValue().get(redisKey);

        if (json == null) return Optional.empty();

        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (IOException e) {
            throw new RedisSerializationException("Ошибка десериализации объекта из Redis", e);
        }
    }

    public void delete(RedisKey key, String... args) {
        String redisKey = key.buildKey(args);
        redisTemplate.delete(redisKey);
    }
}
