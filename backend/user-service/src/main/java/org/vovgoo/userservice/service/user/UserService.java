package org.vovgoo.userservice.service.user;

import org.vovgoo.userservice.dto.user.request.UserUpdateRequest;
import org.vovgoo.userservice.dto.user.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUserProfile();
    UserResponse updateCurrentUserProfile(UserUpdateRequest userUpdateRequest);
}
