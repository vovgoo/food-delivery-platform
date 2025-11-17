package org.vovgoo.userservice.service.security.jwt.strategy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.config.security.property.jwt.JwtExpirationProperty;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;

import com.nimbusds.jose.jwk.RSAKey;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class AccessTokenStrategy extends TokenStrategy {

    public AccessTokenStrategy(RSAKey rsaKey, JwtExpirationProperty jwtExpirationProperty) {
        super(rsaKey, jwtExpirationProperty);
    }

    @Override
    public JwtTokenType getType() {
        return JwtTokenType.ACCESS;
    }

    @Override
    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim("userId", user.getId())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .claim("type", JwtTokenType.ACCESS)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtExpirationProperty.getAccessMs()))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }
}
