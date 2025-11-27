package org.vovgoo.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.vovgoo.common.config.InternalServiceTokens;

@SpringBootApplication(scanBasePackages = {"org.vovgoo.userservice", "org.vovgoo.common.client", "org.vovgoo.common.security"})
@EnableConfigurationProperties(InternalServiceTokens.class)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
