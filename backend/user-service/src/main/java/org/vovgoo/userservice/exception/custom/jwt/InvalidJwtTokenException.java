package org.vovgoo.userservice.exception.custom.jwt;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super("Невалидный JWT токен");
    }
}
