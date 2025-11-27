package org.vovgoo.common.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;

import java.util.UUID;

@FeignClient(
    name = "user-service",
    contextId = "internalUserClient",
    path = "/internal/users"
)
public interface InternalUserClient {

    @GetMapping("/{userId}")
    UserInternalResponse getUser(@PathVariable("userId") UUID userId);
}
