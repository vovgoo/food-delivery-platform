package org.vovgoo.orderservice.service.address;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vovgoo.dto.address.AddressShortResponse;

import java.util.UUID;

@FeignClient(name = "user-service", path = "/internal/addresses")
public interface InternalAddressClient {

    @GetMapping("/{userId}/{addressId}")
    AddressShortResponse getUserAddressAnyStatus(@PathVariable("userId") UUID userId, @PathVariable("addressId") UUID addressId);
}
