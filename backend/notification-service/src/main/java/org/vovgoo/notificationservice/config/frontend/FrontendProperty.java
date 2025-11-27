package org.vovgoo.notificationservice.config.frontend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
@ConfigurationProperties(prefix = "app.frontend")
public class FrontendProperty {

    private String host;
    private Routes routes;

    @Data
    public static class Routes {
        private String emailChange;
    }

    public String buildEmailChangeLink(UUID token) {
        return host + routes.getEmailChange() + "?token=" + token.toString();
    }
}
