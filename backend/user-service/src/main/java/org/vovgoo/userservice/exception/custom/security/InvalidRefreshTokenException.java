package org.vovgoo.userservice.exception.custom.security;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Невалидный refresh токен");
    }
}
