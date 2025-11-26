package org.vovgoo.notificationservice.config.frontend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.frontend")
public class FrontendProperty {
    private String emailChangeRoute;
}
