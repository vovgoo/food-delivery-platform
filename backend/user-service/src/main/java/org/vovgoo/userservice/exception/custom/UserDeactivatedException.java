package org.vovgoo.userservice.exception.custom;

public class UserDeactivatedException extends RuntimeException {
    public UserDeactivatedException() {
        super("Пользователь деактивирован");
    }
}
