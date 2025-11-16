package org.vovgoo.userservice.config.security.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt.keys")
public class JwtKeysProperty {
    private String privateKey;
    private String publicKey;
}
