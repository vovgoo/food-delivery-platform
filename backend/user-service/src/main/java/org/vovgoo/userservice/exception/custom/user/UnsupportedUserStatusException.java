package org.vovgoo.userservice.exception.custom.user;

import org.vovgoo.userservice.entity.enums.UserStatus;

public class UnsupportedUserStatusException extends RuntimeException {
    public UnsupportedUserStatusException(UserStatus status) {
        super("Неподдерживаемый статус пользователя: " + status);
    }
}
