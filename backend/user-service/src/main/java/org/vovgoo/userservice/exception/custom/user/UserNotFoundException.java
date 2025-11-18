package org.vovgoo.userservice.exception.custom.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
