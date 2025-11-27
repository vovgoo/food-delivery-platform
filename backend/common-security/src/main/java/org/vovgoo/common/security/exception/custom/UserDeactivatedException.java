package org.vovgoo.common.security.exception.custom;

public class UserDeactivatedException extends RuntimeException {
    public UserDeactivatedException() {
        super("Пользователь деактивирован");
    }
}
