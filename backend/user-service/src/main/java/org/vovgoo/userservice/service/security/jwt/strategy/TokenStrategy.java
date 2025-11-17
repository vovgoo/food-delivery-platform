package org.vovgoo.userservice.service.security.jwt.strategy;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.*;
import org.vovgoo.userservice.config.security.property.jwt.JwtExpirationProperty;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.InvalidJwtTokenException;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public abstract class TokenStrategy {

    protected final RSAKey rsaKey;
    protected final JwtExpirationProperty jwtExpirationProperty;

    protected TokenStrategy(RSAKey rsaKey, JwtExpirationProperty jwtExpirationProperty) {
        this.rsaKey = rsaKey;
        this.jwtExpirationProperty = jwtExpirationProperty;
    }

    protected RSAPrivateKey getPrivateKey() {
        try {
            return rsaKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to get RSA private key", e);
        }
    }

    protected RSAPublicKey getPublicKey() {
        try {
            return rsaKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to get RSA public key", e);
        }
    }

    public abstract String generateToken(User user);

    public abstract JwtTokenType getType();

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return getType().name().equals(claims.get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    protected Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException();
        }
    }

    public UUID extractUserId(String token) {
        return parseClaims(token).get("userId", UUID.class);
    }
}
