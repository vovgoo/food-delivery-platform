package org.vovgoo.restaurantservice.config.image;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.image.limit")
public class ImageLimitProperties {
    private int restaurant;
    private int dish;
}
