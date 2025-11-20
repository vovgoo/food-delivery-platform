package org.vovgoo.user.exception;

public class UserActiveException extends RuntimeException {
    public UserActiveException() {
        super("Пользователь уже активен");
    }
}
