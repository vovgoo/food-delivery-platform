package org.vovgoo.userservice.exception.custom;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("Пользователь заблокирован");
    }
}
