package org.vovgoo.user.client;

import org.vovgoo.dto.user.UserInternalResponse;

import java.util.UUID;

public interface UserService {
    UserInternalResponse getUser(UUID userId);
}
