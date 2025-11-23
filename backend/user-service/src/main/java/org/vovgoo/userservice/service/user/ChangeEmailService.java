package org.vovgoo.userservice.service.user;

import org.vovgoo.userservice.dto.user.request.ChangeEmailRequest;
import org.vovgoo.userservice.dto.user.request.ConfirmChangeEmailRequest;

public interface ChangeEmailService {
    void changeEmail(ChangeEmailRequest request);
    void confirmChangeEmail(ConfirmChangeEmailRequest request);
}
