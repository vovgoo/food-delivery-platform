package org.vovgoo.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.vovgoo.restaurantservice", "org.vovgoo.config", "org.vovgoo.security", "org.vovgoo.user"})
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }

}
