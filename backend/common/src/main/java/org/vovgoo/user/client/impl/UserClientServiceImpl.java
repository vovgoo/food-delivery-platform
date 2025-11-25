package org.vovgoo.user.client.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.user.UserInternalResponse;
import org.vovgoo.user.client.InternalUserClient;
import org.vovgoo.user.client.UserClientService;
import org.vovgoo.user.exception.UserNotFoundException;
import org.vovgoo.user.exception.UserServiceException;

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
            throw new UserNotFoundException();
        } catch (FeignException e) {
            throw new UserServiceException();
        }
    }
}
