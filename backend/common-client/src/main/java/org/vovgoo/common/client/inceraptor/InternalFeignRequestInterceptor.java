package org.vovgoo.common.client.inceraptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vovgoo.common.config.InternalServiceTokens;

@Component
@RequiredArgsConstructor
public class InternalFeignRequestInterceptor implements RequestInterceptor {

    private final InternalServiceTokens internalServiceTokens;

    @Value("${spring.application.name}")
    private String selfServiceName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String targetService = requestTemplate.feignTarget().name();
        String token = internalServiceTokens.getTokens().get(targetService);

        if (token != null) {
            requestTemplate.header("X-Service-Name", selfServiceName);
            requestTemplate.header("X-Internal-Token", token);
        }
    }
}
