package org.vovgoo.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vovgoo.enums.user.UserStatus;

import java.util.UUID;

@FeignClient(
    name = "user-service",
    contextId = "internalUserClient",
    path = "/internal/users"
)
public interface InternalUserClient {

    @GetMapping("/{userId}/status")
    UserStatus getUserStatus(@PathVariable("userId") UUID userId);
}
