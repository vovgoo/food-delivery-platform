package org.vovgoo.common.client.emailDomain;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        contextId = "internalEmailDomainClient",
        path = "/internal/email/domain"
)
public interface InternalEmailDomainClient {

    @GetMapping("/check/{domain}")
    Boolean checkDomain(@PathVariable("domain") String domain);
}
