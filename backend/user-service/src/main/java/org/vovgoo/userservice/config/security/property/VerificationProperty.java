package org.vovgoo.userservice.config.security.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.verification")
public class VerificationProperty {

    private Phone phone;
    private Email email;

    @Data
    public static class Phone {
        private int maxAttempts;
    }

    @Data
    public static class Email {
        private int maxAttempts;
    }
}
