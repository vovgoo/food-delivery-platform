package org.vovgoo.userservice.exception.custom.verification;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Неверный токен");
    }
}
