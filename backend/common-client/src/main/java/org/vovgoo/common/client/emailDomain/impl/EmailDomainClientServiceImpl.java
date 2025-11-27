package org.vovgoo.common.client.emailDomain.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.emailDomain.InternalEmailDomainClient;
import org.vovgoo.common.client.emailDomain.EmailDomainClientService;
import org.vovgoo.common.client.exception.custom.FeignServiceException;

@Service
@RequiredArgsConstructor
public class EmailDomainClientServiceImpl implements EmailDomainClientService {

    private final InternalEmailDomainClient internalEmailDomainClient;

    @Override
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public Boolean checkDomain(String domain) {
        try {
            return internalEmailDomainClient.checkDomain(domain);
        } catch (FeignException e) {
            throw new FeignServiceException("Ошибка получения данных о домене", e);
        }
    }
}
