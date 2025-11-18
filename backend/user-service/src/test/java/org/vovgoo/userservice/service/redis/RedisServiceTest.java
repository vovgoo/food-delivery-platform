package org.vovgoo.userservice.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.exception.custom.messaging.RedisKeyTypeMismatchException;
import org.vovgoo.userservice.exception.custom.messaging.RedisSerializationException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    private RedisService redisService;
    private StringRedisTemplate redisTemplate;
    private ObjectMapper objectMapper;
    private ValueOperations<String, String> valueOps;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        valueOps = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        redisService = new RedisService(redisTemplate, objectMapper);
    }

    @Test
    void set_shouldStoreValue() throws JsonProcessingException {
        String value = "some-value";
        when(objectMapper.writeValueAsString(value)).thenReturn("\"some-value\"");

        redisService.set(RedisKey.REFRESH_TOKEN, value, "user123");

        String expectedKey = RedisKey.REFRESH_TOKEN.buildKey("user123");
        verify(valueOps).set(eq(expectedKey), eq("\"some-value\""), eq(RedisKey.REFRESH_TOKEN.getTtl()));
    }

    @Test
    void set_shouldThrowTypeMismatch() {
        Integer value = 123;
        assertThrows(RedisKeyTypeMismatchException.class,
                () -> redisService.set(RedisKey.REFRESH_TOKEN, value, "user123"));
    }

    @Test
    void set_shouldThrowSerializationException() throws JsonProcessingException {
        String value = "test";
        when(objectMapper.writeValueAsString(value)).thenThrow(JsonProcessingException.class);

        assertThrows(RedisSerializationException.class,
                () -> redisService.set(RedisKey.REFRESH_TOKEN, value, "user123"));
    }

    @Test
    void get_shouldReturnValue() throws IOException {
        String json = "\"some-value\"";
        when(valueOps.get(anyString())).thenReturn(json);
        when(objectMapper.readValue(json, String.class)).thenReturn("some-value");

        Optional<String> result = redisService.get(RedisKey.REFRESH_TOKEN, String.class, "user123");

        assertTrue(result.isPresent());
        assertEquals("some-value", result.get());
    }

    @Test
    void get_shouldReturnEmptyWhenKeyNotFound() {
        when(valueOps.get(anyString())).thenReturn(null);

        Optional<String> result = redisService.get(RedisKey.REFRESH_TOKEN, String.class, "user123");

        assertTrue(result.isEmpty());
    }

    @Test
    void get_shouldThrowTypeMismatch() {
        assertThrows(RedisKeyTypeMismatchException.class,
                () -> redisService.get(RedisKey.REFRESH_TOKEN, Integer.class, "user123"));
    }

    @Test
    void get_shouldThrowDeserializationException() throws Exception {
        String json = "\"some-value\"";
        when(valueOps.get(anyString())).thenReturn(json);

        doAnswer(invocation -> { throw new IOException(); })
                .when(objectMapper).readValue(json, String.class);

        assertThrows(RedisSerializationException.class,
                () -> redisService.get(RedisKey.REFRESH_TOKEN, String.class, "user123"));
    }

    @Test
    void delete_shouldCallRedisDelete() {
        redisService.delete(RedisKey.REFRESH_TOKEN, "user123");

        String expectedKey = RedisKey.REFRESH_TOKEN.buildKey("user123");
        verify(redisTemplate).delete(expectedKey);
    }
}
