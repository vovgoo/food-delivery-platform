package org.vovgoo.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.vovgoo.common.config.InternalServiceTokens;

@SpringBootApplication(scanBasePackages = {"org.vovgoo.restaurantservice", "org.vovgoo.common.client", "org.vovgoo.common.security"})
@EnableConfigurationProperties(InternalServiceTokens.class)
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }

}
