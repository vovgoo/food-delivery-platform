package org.vovgoo.orderservice.service.address;

import org.vovgoo.dto.address.AddressInternalResponse;

import java.util.UUID;

public interface AddressClientService {
    AddressInternalResponse getAddress(UUID userId, UUID addressId);
    void validateAddress(UUID userId, UUID addressId);
}
