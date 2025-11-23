package org.vovgoo.userservice.service.user;

import org.vovgoo.userservice.dto.user.request.ChangePhoneRequest;
import org.vovgoo.userservice.dto.user.request.ConfirmChangePhoneRequest;

public interface ChangePhoneService {
    void changePhone(ChangePhoneRequest request);
    void confirmChangePhone(ConfirmChangePhoneRequest request);
}
