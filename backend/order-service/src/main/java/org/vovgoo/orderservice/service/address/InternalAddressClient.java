package org.vovgoo.orderservice.service.address;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vovgoo.dto.address.AddressInternalResponse;

import java.util.UUID;

@FeignClient(
    name = "user-service",
    contextId = "internalAddressClient",
    path = "/internal/addresses"
)
public interface InternalAddressClient {

    @GetMapping("/{userId}/{addressId}")
    AddressInternalResponse getUserAddressAnyStatus(@PathVariable("userId") UUID userId, @PathVariable("addressId") UUID addressId);
}
