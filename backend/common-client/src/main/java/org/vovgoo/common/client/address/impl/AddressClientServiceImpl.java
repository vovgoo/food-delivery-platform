package org.vovgoo.common.client.address.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.address.AddressClientService;
import org.vovgoo.common.client.address.InternalAddressClient;
import org.vovgoo.common.client.exception.custom.FeignServiceException;
import org.vovgoo.common.client.exception.custom.NotFoundException;
import org.vovgoo.common.domain.address.dto.AddressInternalResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressClientServiceImpl implements AddressClientService {

    private final InternalAddressClient internalAddressClient;

    @Override
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public AddressInternalResponse getAddress(UUID userId, UUID addressId) {
        try {
            return internalAddressClient.getUserAddressAnyStatus(userId, addressId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Адрес не найден", e);
        } catch (FeignException e) {
            throw new FeignServiceException("Ошибка получения данных о адресе", e);
        }
    }
}
