package org.vovgoo.userservice.service.security.jwt.strategy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.property.JwtExpirationProperty;
import org.vovgoo.userservice.entity.User;

import com.nimbusds.jose.jwk.RSAKey;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.Date;
import java.util.UUID;

@Component
public class RefreshTokenStrategy extends TokenStrategy {

    private final RedisService redisService;

    public RefreshTokenStrategy(RSAKey rsaKey, JwtExpirationProperty jwtExpirationProperty, RedisService redisService) {
        super(rsaKey, jwtExpirationProperty);
        this.redisService = redisService;
    }

    @Override
    public JwtTokenType getType() {
        return JwtTokenType.REFRESH;
    }

    @Override
    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .claim("userId", user.getId())
                .claim("type", JwtTokenType.REFRESH.name())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtExpirationProperty.getRefreshMs()))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();

        redisService.set(RedisKey.REFRESH_TOKEN, token, String.valueOf(user.getId()));

        return token;
    }

    @Override
    public boolean validateToken(String token) {
        if (!super.validateToken(token)) return false;

        UUID userId = extractUserId(token);

        return redisService.get(RedisKey.REFRESH_TOKEN, String.class, String.valueOf(userId))
                .map(storedToken -> storedToken.equals(token))
                .orElse(false);
    }
}
