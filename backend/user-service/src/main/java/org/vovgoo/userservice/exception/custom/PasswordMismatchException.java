package org.vovgoo.userservice.exception.custom;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Старый пароль неверный");
    }
}
