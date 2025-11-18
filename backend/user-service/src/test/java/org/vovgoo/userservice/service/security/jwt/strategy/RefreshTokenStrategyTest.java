package org.vovgoo.userservice.service.security.jwt.strategy;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.property.jwt.JwtExpirationProperty;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenStrategyTest {
    private RefreshTokenStrategy refreshTokenStrategy;
    private RSAKey rsaKey;
    private JwtExpirationProperty jwtExpirationProperty;
    private RedisService redisService;

    @BeforeEach
    void setUp() throws JOSEException {
        rsaKey = new RSAKeyGenerator(2048)
                .keyID("test-key")
                .generate();

        jwtExpirationProperty = new JwtExpirationProperty();
        jwtExpirationProperty.setRefreshMs(7 * 24 * 60 * 60_000L);

        redisService = mock(RedisService.class);

        refreshTokenStrategy = new RefreshTokenStrategy(rsaKey, jwtExpirationProperty, redisService);
    }

    @Test
    void generateToken_shouldReturnValidTokenAndSaveToRedis() {
        User user = new User();
        user.setId(UUID.randomUUID());

        String token = refreshTokenStrategy.generateToken(user);

        assertNotNull(token);
        verify(redisService, times(1))
                .set(eq(RedisKey.REFRESH_TOKEN), eq(token), eq(String.valueOf(user.getId())));
    }

    @Test
    void validateToken_shouldReturnTrueForValidTokenInRedis() {
        User user = new User();
        user.setId(UUID.randomUUID());

        String token = refreshTokenStrategy.generateToken(user);

        when(redisService.get(eq(RedisKey.REFRESH_TOKEN), eq(String.class), eq(String.valueOf(user.getId()))))
                .thenReturn(Optional.of(token));

        assertTrue(refreshTokenStrategy.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForValidTokenNotInRedis() {
        User user = new User();
        user.setId(UUID.randomUUID());

        String token = refreshTokenStrategy.generateToken(user);

        when(redisService.get(eq(RedisKey.REFRESH_TOKEN), eq(String.class), eq(String.valueOf(user.getId()))))
                .thenReturn(Optional.empty());

        assertFalse(refreshTokenStrategy.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForCorruptedToken() {
        assertFalse(refreshTokenStrategy.validateToken("invalid.token.value"));
    }

    @Test
    void validateToken_shouldReturnFalseForWrongType() {
        User user = new User();
        user.setId(UUID.randomUUID());

        String token = refreshTokenStrategy.generateToken(user);

        RefreshTokenStrategy fakeStrategy = new RefreshTokenStrategy(rsaKey, jwtExpirationProperty, redisService) {
            @Override
            public JwtTokenType getType() {
                return JwtTokenType.ACCESS;
            }
        };

        when(redisService.get(any(), any(), anyString())).thenReturn(Optional.of(token));

        assertFalse(fakeStrategy.validateToken(token));
    }

    @Test
    void extractUserId_shouldReturnCorrectUuid() {
        User user = new User();
        user.setId(UUID.randomUUID());

        String token = refreshTokenStrategy.generateToken(user);
        UUID extracted = refreshTokenStrategy.extractUserId(token);

        assertEquals(user.getId(), extracted);
    }

    @Test
    void extractUserId_shouldThrowForInvalidUuid() {
        RefreshTokenStrategy fakeStrategy = new RefreshTokenStrategy(rsaKey, jwtExpirationProperty, redisService) {
            @Override
            public String generateToken(User user) {
                return Jwts.builder()
                        .claim("userId", "not-a-uuid")
                        .claim("type", JwtTokenType.REFRESH.name())
                        .signWith(getPrivateKey())
                        .compact();
            }
        };

        String token = fakeStrategy.generateToken(new User());

        assertThrows(IllegalArgumentException.class, () -> fakeStrategy.extractUserId(token));
    }
}