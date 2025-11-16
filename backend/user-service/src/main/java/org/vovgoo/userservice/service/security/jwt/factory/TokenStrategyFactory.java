package org.vovgoo.userservice.service.security.jwt.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.service.security.jwt.strategy.TokenStrategy;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenStrategyFactory {

    private final List<TokenStrategy> strategies;

    public TokenStrategy getStrategy(JwtTokenType type) {
        return strategies.stream()
                .filter(s -> s.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy for type " + type));
    }
}
