package org.vovgoo.common.client.user.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.exception.custom.FeignServiceException;
import org.vovgoo.common.client.exception.custom.NotFoundException;
import org.vovgoo.common.client.user.InternalUserClient;
import org.vovgoo.common.client.user.UserClientService;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final InternalUserClient internalUserClient;

    @Override
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public UserInternalResponse getUser(UUID userId) {
        try {
            return internalUserClient.getUser(userId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Пользователь не найден", e);
        } catch (FeignException e) {
            throw new FeignServiceException("Ошибка получения данных о пользователе", e);
        }
    }
}
