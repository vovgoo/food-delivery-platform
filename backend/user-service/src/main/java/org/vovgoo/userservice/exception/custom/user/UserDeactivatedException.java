package org.vovgoo.userservice.exception.custom.user;

public class UserDeactivatedException extends RuntimeException {
    public UserDeactivatedException() {
        super("Пользователь деактивирован");
    }
}
