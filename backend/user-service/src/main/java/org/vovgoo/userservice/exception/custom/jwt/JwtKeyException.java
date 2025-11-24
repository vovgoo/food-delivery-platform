package org.vovgoo.userservice.exception.custom.jwt;

public class JwtKeyException extends RuntimeException {
    public JwtKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
