package org.vovgoo.userservice.exception.custom.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
