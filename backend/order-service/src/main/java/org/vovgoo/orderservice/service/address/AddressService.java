package org.vovgoo.orderservice.service.address;

import org.vovgoo.dto.address.AddressShortResponse;

import java.util.UUID;

public interface AddressService {
    AddressShortResponse getAddress(UUID userId, UUID addressId);
    AddressShortResponse getValidAddress(UUID userId, UUID addressId);
}
