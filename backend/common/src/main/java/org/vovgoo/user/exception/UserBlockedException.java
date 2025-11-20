package org.vovgoo.user.exception;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("Пользователь заблокирован");
    }
}
