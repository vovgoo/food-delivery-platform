package org.vovgoo.orderservice.config.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"org.vovgoo.orderservice.service.restaurant", "org.vovgoo.orderservice.service.address"})
public class FeignConfig {
}
