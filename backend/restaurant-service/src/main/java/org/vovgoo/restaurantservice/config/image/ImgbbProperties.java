package org.vovgoo.restaurantservice.config.image;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.imgbb")
public class ImgbbProperties {
    private String apiKey;
    private String endpoint;
}
