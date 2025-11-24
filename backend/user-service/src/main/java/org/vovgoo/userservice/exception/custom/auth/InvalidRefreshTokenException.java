package org.vovgoo.userservice.exception.custom.auth;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Невалидный refresh токен");
    }
}
