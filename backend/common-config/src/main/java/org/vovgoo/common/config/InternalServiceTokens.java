package org.vovgoo.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.internal-services")
public class InternalServiceTokens {
    private Map<String, String> tokens;
    private String self;
}
