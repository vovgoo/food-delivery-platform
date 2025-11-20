package org.vovgoo.config.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.vovgoo.user.client")
public class FeignCommonConfig {
}