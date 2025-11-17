package org.vovgoo.userservice.exception.custom;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super("Невалидный JWT токен");
    }
}
