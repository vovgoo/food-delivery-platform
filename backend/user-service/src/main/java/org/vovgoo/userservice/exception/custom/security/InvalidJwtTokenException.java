package org.vovgoo.userservice.exception.custom.security;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super("Невалидный JWT токен");
    }
}
