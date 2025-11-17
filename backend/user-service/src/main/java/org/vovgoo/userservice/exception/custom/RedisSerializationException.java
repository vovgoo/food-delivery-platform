package org.vovgoo.userservice.exception.custom;

public class RedisSerializationException extends RuntimeException {
    public RedisSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}