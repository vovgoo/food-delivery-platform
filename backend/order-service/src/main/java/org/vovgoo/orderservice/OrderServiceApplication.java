package org.vovgoo.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.vovgoo.common.config.InternalServiceTokens;

@SpringBootApplication(scanBasePackages = {"org.vovgoo.orderservice", "org.vovgoo.common.client", "org.vovgoo.common.security"})
@EnableConfigurationProperties(InternalServiceTokens.class)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
