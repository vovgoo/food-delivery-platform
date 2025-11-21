package org.vovgoo.userservice.service.address;

import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;

import java.util.UUID;

public interface AddressService {
    PageResponse<AddressResponse> getAll(PageParams pageParams);
    AddressResponse create(CreateAddressRequest createAddressRequest);
    void remove(UUID id);
    void setDefault(UUID id);
    AddressShortResponse getActiveUserAddress(UUID userId, UUID addressId);
    AddressShortResponse getUserAddress(UUID userId, UUID addressId);
}
