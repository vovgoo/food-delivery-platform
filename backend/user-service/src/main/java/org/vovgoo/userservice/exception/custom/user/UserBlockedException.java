package org.vovgoo.userservice.exception.custom.user;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("Пользователь заблокирован");
    }
}
