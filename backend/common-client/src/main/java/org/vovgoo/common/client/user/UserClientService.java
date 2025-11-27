package org.vovgoo.common.client.user;

import org.vovgoo.common.domain.user.dto.UserInternalResponse;

import java.util.UUID;

public interface UserClientService {
    UserInternalResponse getUser(UUID userId);
}
