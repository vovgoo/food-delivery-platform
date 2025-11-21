package org.vovgoo.orderservice.service.address.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.enums.address.AddressStatus;
import org.vovgoo.orderservice.exception.custom.address.AddressDeletedException;
import org.vovgoo.orderservice.exception.custom.address.AddressNotFoundException;
import org.vovgoo.orderservice.exception.custom.address.AddressServiceException;
import org.vovgoo.orderservice.service.address.AddressService;
import org.vovgoo.orderservice.service.address.InternalAddressClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final InternalAddressClient internalAddressClient;

    @Override
    public AddressShortResponse  getAddress(UUID userId, UUID addressId) {
        try {
            AddressShortResponse address = internalAddressClient.getUserAddressAnyStatus(userId, addressId);
            if (address == null) {
                throw new AddressNotFoundException();
            }
            return address;
        } catch (FeignException.NotFound e) {
            throw new AddressNotFoundException();
        } catch (FeignException e) {
            throw new AddressServiceException();
        }
    }

    @Override
    public void validateAddress(UUID userId, UUID addressId) {
        AddressShortResponse address = getAddress(userId, addressId);

        if (AddressStatus.DELETED.equals(address.addressStatus())) {
            throw new AddressDeletedException();
        }
    }
}
