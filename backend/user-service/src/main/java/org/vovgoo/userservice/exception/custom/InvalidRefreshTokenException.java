package org.vovgoo.userservice.exception.custom;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Невалидный refresh токен");
    }
}
