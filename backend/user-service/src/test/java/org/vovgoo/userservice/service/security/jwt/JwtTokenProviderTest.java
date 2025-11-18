package org.vovgoo.userservice.service.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;
import org.vovgoo.userservice.service.security.jwt.factory.TokenStrategyFactory;
import org.vovgoo.userservice.service.security.jwt.strategy.TokenStrategy;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private TokenStrategyFactory factory;
    private TokenStrategy accessStrategy;
    private JwtTokenProvider provider;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        factory = mock(TokenStrategyFactory.class);
        accessStrategy = mock(TokenStrategy.class);

        provider = new JwtTokenProvider(factory);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);

        when(factory.getStrategy(JwtTokenType.ACCESS)).thenReturn(accessStrategy);
    }

    @Test
    void generateToken_shouldReturnToken() {
        when(accessStrategy.generateToken(user)).thenReturn("mock-token");

        String token = provider.generateToken(JwtTokenType.ACCESS, user);

        assertEquals("mock-token", token);
        verify(accessStrategy, times(1)).generateToken(user);
    }

    @Test
    void validateToken_shouldReturnTrue() {
        when(accessStrategy.validateToken("valid-token")).thenReturn(true);

        boolean result = provider.validateToken(JwtTokenType.ACCESS, "valid-token");

        assertTrue(result);
        verify(accessStrategy, times(1)).validateToken("valid-token");
    }

    @Test
    void validateToken_shouldReturnFalse() {
        when(accessStrategy.validateToken("invalid-token")).thenReturn(false);

        boolean result = provider.validateToken(JwtTokenType.ACCESS, "invalid-token");

        assertFalse(result);
        verify(accessStrategy, times(1)).validateToken("invalid-token");
    }

    @Test
    void extractUserId_shouldReturnUserIdAsString() {
        when(accessStrategy.extractUserId("token")).thenReturn(userId);

        String result = String.valueOf(provider.extractUserId(JwtTokenType.ACCESS, "token"));

        assertEquals(userId.toString(), result);
        verify(accessStrategy, times(1)).extractUserId("token");
    }

    @Test
    void factoryThrowsException_shouldPropagate() {
        when(factory.getStrategy(JwtTokenType.ACCESS))
                .thenThrow(new RuntimeException("Strategy not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> provider.generateToken(JwtTokenType.ACCESS, user));

        assertEquals("Strategy not found", ex.getMessage());
    }
}