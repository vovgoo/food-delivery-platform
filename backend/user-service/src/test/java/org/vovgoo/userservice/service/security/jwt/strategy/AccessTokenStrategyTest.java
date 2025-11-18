package org.vovgoo.userservice.service.security.jwt.strategy;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vovgoo.userservice.config.security.property.jwt.JwtExpirationProperty;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.RoleType;
import org.vovgoo.userservice.exception.custom.security.InvalidJwtTokenException;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenStrategyTest {
    private AccessTokenStrategy accessTokenStrategy;
    private RSAKey rsaKey;
    private JwtExpirationProperty jwtExpirationProperty;

    @BeforeEach
    void setUp() throws JOSEException {
        rsaKey = new RSAKeyGenerator(2048)
                .keyID("test-key")
                .generate();

        jwtExpirationProperty = new JwtExpirationProperty();
        jwtExpirationProperty.setAccessMs(3600_000L);

        accessTokenStrategy = new AccessTokenStrategy(rsaKey, jwtExpirationProperty);
    }

    @Test
    void generateToken_shouldReturnValidJwt() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(RoleType.valueOf("ADMIN"));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRoles(Set.of(role));

        String token = accessTokenStrategy.generateToken(user);

        assertNotNull(token);
        assertTrue(accessTokenStrategy.validateToken(token));
        assertEquals(user.getId(), accessTokenStrategy.extractUserId(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertFalse(accessTokenStrategy.validateToken("invalid.token.value"));
    }

    @Test
    void validateToken_shouldReturnFalseForWrongType() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(RoleType.valueOf("ADMIN"));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRoles(Set.of(role));

        String token = accessTokenStrategy.generateToken(user);

        TokenStrategy fakeStrategy = new TokenStrategy(rsaKey, jwtExpirationProperty) {
            @Override
            public String generateToken(User user) {
                return token;
            }

            @Override
            public JwtTokenType getType() {
                return JwtTokenType.REFRESH;
            }
        };

        assertFalse(fakeStrategy.validateToken(token));
    }

    @Test
    void extractUserId_shouldThrowForInvalidUuid() {
        TokenStrategy fakeStrategy = new TokenStrategy(rsaKey, jwtExpirationProperty) {
            @Override
            public String generateToken(User user) {
                return Jwts.builder()
                        .claim("userId", "not-a-uuid")
                        .claim("type", JwtTokenType.ACCESS)
                        .signWith(getPrivateKey())
                        .compact();
            }

            @Override
            public JwtTokenType getType() {
                return JwtTokenType.ACCESS;
            }
        };

        String token = fakeStrategy.generateToken(new User());
        assertThrows(IllegalArgumentException.class, () -> fakeStrategy.extractUserId(token));
    }

    @Test
    void parseClaims_shouldThrowInvalidJwtTokenExceptionForCorruptedToken() {
        assertThrows(InvalidJwtTokenException.class,
                () -> accessTokenStrategy.parseClaims("corrupted.token.value"));
    }

    @Test
    void getType_shouldReturnAccess() {
        assertEquals(JwtTokenType.ACCESS, accessTokenStrategy.getType());
    }
}