package org.vovgoo.userservice.service.security.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.property.JwtProperty;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.InvalidRefreshTokenException;
import org.vovgoo.userservice.service.security.JwtService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperty jwtProperty;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getAccessExpirationMs()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getRefreshExpirationMs()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String refreshAccessToken(String refreshToken, User user) {
        if (!validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Refresh токен недействителен или истёк");
        }

        if (!isRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Переданный токен не является refresh-токеном");
        }

        String email = getEmailFromToken(refreshToken);
        if (!email.equals(user.getEmail())) {
            throw new InvalidRefreshTokenException("Refresh-токен не принадлежит этому пользователю");
        }

        return generateAccessToken(user);
    }

    private boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return "refresh".equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
