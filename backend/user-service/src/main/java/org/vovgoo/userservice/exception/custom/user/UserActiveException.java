package org.vovgoo.userservice.exception.custom.user;

public class UserActiveException extends RuntimeException {
    public UserActiveException() {
        super("Пользователь уже активен");
    }
}
