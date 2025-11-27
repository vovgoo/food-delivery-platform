package org.vovgoo.orderservice.service.address.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.address.AddressClientService;
import org.vovgoo.common.domain.address.dto.AddressInternalResponse;
import org.vovgoo.common.domain.address.enums.AddressStatus;
import org.vovgoo.orderservice.exception.custom.address.AddressDeletedException;
import org.vovgoo.orderservice.service.address.AddressService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressClientService addressClientService;

    @Override
    public AddressInternalResponse getAddress(UUID userId, UUID addressId) {
        return addressClientService.getAddress(userId, addressId);
    }

    @Override
    public void validateAddress(UUID userId, UUID addressId) {
        AddressInternalResponse address = addressClientService.getAddress(userId, addressId);

        if (AddressStatus.DELETED.equals(address.addressStatus())) {
            throw new AddressDeletedException();
        }
    }
}
