package org.vovgoo.apigateway.config.jwt;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        WebClient lbWebClient = WebClient.builder()
                .filter(lbFunction)
                .build();

        return NimbusReactiveJwtDecoder
                .withJwkSetUri("lb://user-service/.well-known/jwks.json")
                .webClient(lbWebClient)
                .build();
    }
}
