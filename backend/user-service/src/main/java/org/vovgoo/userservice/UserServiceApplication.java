package org.vovgoo.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.vovgoo.userservice", "org.vovgoo.common.client", "org.vovgoo.common.security", "org.vovgoo.common.config"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
