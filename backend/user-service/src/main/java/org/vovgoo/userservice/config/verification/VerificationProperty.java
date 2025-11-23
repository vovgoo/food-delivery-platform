package org.vovgoo.userservice.config.verification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.verification")
public class VerificationProperty {

    private Attempts attempts = new Attempts();

    @Data
    public static class Attempts {
        private int phone = 10;
        private int email = 10;
    }
}
