package org.vovgoo.userservice.service.security.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.property.JwtExpirationProperty;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.InvalidRefreshTokenException;
import org.vovgoo.userservice.service.security.JwtService;
import org.vovgoo.userservice.service.security.enums.JwtTokenType;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RSAKey rsaKey;
    private final JwtExpirationProperty jwtExpirationProperty;

    private RSAPrivateKey getPrivateKey() {
        try {
            return rsaKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Не удалось получить приватный ключ RSA для подписи JWT", e);
        }
    }

    private RSAPublicKey getPublicKey() {
        try {
            return rsaKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Не удалось получить приватный ключ RSA для подписи JWT", e);
        }
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .claim("type", JwtTokenType.ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationProperty.getAccessMs()))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", JwtTokenType.REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationProperty.getRefreshMs()))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
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
                .setSigningKey(getPublicKey())
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
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return JwtTokenType.REFRESH.equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
