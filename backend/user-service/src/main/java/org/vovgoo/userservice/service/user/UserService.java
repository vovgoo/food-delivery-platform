package org.vovgoo.userservice.service.user;

import org.vovgoo.common.domain.user.dto.UserInternalResponse;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserInternalResponse getUser(UUID userId);
    UserResponse getProfile();
    UserResponse updateUserProfile(UpdateUserProfileRequest request);
    void changePassword(ChangePasswordRequest request);
    void deactivateAccount();
    void reactivateAccount();
}
