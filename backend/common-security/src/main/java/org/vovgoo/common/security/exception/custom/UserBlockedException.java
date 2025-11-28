package org.vovgoo.common.security.exception.custom;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("Пользователь заблокирован");
    }
}
