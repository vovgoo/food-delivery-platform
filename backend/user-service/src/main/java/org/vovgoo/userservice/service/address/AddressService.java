package org.vovgoo.userservice.service.address;

import org.vovgoo.common.domain.address.dto.AddressInternalResponse;
import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
import org.vovgoo.userservice.dto.address.request.CreateAddressRequest;
import org.vovgoo.userservice.dto.address.response.AddressResponse;

import java.util.UUID;

public interface AddressService {
    PageResponse<AddressResponse> getAll(PageParams pageParams);
    AddressResponse create(CreateAddressRequest createAddressRequest);
    void remove(UUID id);
    void setDefault(UUID id);
    AddressInternalResponse getUserAddress(UUID userId, UUID addressId);
}
