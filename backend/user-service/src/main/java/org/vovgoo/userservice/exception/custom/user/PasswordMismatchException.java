package org.vovgoo.userservice.exception.custom.user;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Старый пароль неверный");
    }
}
