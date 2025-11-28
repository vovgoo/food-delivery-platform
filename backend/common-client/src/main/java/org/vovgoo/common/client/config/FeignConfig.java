package org.vovgoo.common.client.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
        "org.vovgoo.common.client.user",
        "org.vovgoo.common.client.address",
        "org.vovgoo.common.client.emailDomain",
        "org.vovgoo.common.client.restaurant",
})
public class FeignConfig {
}