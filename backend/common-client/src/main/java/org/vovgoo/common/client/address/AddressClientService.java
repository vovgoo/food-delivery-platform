package org.vovgoo.common.client.address;

import org.vovgoo.common.domain.address.dto.AddressInternalResponse;

import java.util.UUID;

public interface AddressClientService {
    AddressInternalResponse getAddress(UUID userId, UUID addressId);
}
