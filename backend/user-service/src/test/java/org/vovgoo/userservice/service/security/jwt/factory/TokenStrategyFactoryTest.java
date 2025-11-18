package org.vovgoo.userservice.service.security.jwt.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.exception.custom.security.TokenStrategyNotFoundException;
import org.vovgoo.userservice.service.security.jwt.strategy.TokenStrategy;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenStrategyFactoryTest {

    private TokenStrategy accessStrategy;
    private TokenStrategy refreshStrategy;
    private TokenStrategyFactory factory;

    @BeforeEach
    void setUp() {
        accessStrategy = mock(TokenStrategy.class);
        when(accessStrategy.getType()).thenReturn(JwtTokenType.ACCESS);

        refreshStrategy = mock(TokenStrategy.class);
        when(refreshStrategy.getType()).thenReturn(JwtTokenType.REFRESH);

        factory = new TokenStrategyFactory(List.of(accessStrategy, refreshStrategy));
    }

    @Test
    void getStrategy_shouldReturnAccessStrategy() {
        TokenStrategy strategy = factory.getStrategy(JwtTokenType.ACCESS);
        assertEquals(accessStrategy, strategy);
    }

    @Test
    void getStrategy_shouldReturnRefreshStrategy() {
        TokenStrategy strategy = factory.getStrategy(JwtTokenType.REFRESH);
        assertEquals(refreshStrategy, strategy);
    }

    @Test
    void getStrategy_shouldThrowWhenStrategyNotFound() {
        TokenStrategyFactory singleFactory = new TokenStrategyFactory(List.of(accessStrategy));

        assertThrows(TokenStrategyNotFoundException.class,
                () -> singleFactory.getStrategy(JwtTokenType.REFRESH));
    }
}