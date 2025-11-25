package org.vovgoo.orderservice.service.address.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.address.AddressInternalResponse;
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
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public AddressInternalResponse getAddress(UUID userId, UUID addressId) {
        try {
            AddressInternalResponse address = internalAddressClient.getUserAddressAnyStatus(userId, addressId);
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
        AddressInternalResponse address = getAddress(userId, addressId);

        if (AddressStatus.DELETED.equals(address.addressStatus())) {
            throw new AddressDeletedException();
        }
    }
}
