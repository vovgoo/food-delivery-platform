package org.vovgoo.userservice.service.user;

import org.vovgoo.dto.user.UserInternalResponse;
import org.vovgoo.userservice.dto.user.request.*;
import org.vovgoo.userservice.dto.user.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getProfile();
    UserResponse updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void changePhone(ChangePhoneRequest changePhoneRequest);
    void confirmChangePhone(ConfirmChangePhoneRequest confirmChangePhoneRequest);
    void changeEmail(ChangeEmailRequest changeEmailRequest);
    void confirmChangeEmail(String token);
    void deactivateAccount();
    void reactivateAccount();
    UserInternalResponse getUser(UUID userId);
}
