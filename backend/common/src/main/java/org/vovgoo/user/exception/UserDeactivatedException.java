package org.vovgoo.user.exception;

public class UserDeactivatedException extends RuntimeException {
    public UserDeactivatedException() {
        super("Пользователь деактивирован");
    }
}
