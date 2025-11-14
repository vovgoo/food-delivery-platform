package org.vovgoo.userservice.config.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.expiration")
public class JwtExpirationProperty {
    private long accessMs;
    private long refreshMs;
}
