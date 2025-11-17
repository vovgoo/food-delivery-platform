package org.vovgoo.userservice.service.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;
import org.vovgoo.userservice.service.security.jwt.factory.TokenStrategyFactory;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenStrategyFactory tokenStrategyFactory;

    public String generateToken(JwtTokenType type, User user) {
        return tokenStrategyFactory.getStrategy(type).generateToken(user);
    }

    public boolean validateToken(JwtTokenType type, String token) {
        return tokenStrategyFactory.getStrategy(type).validateToken(token);
    }

    public UUID extractUserId(JwtTokenType type, String token) {
        return tokenStrategyFactory.getStrategy(type).extractUserId(token);
    }
}
