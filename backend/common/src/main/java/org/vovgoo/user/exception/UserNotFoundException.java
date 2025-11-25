package org.vovgoo.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
