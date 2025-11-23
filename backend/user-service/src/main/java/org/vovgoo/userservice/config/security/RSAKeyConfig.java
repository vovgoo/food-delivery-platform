package org.vovgoo.userservice.config.security;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vovgoo.userservice.config.security.property.JwtKeysProperty;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class RSAKeyConfig {

    private final JwtKeysProperty jwtKeysProperty;

    @Bean
    public RSAKey rsaKey() throws Exception {
        byte[] privateBytes = Base64.getDecoder().decode(jwtKeysProperty.getPrivateKey());
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateBytes);

        byte[] publicBytes = Base64.getDecoder().decode(jwtKeysProperty.getPublicKey());
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("user-service-key")
                .build();
    }
}
