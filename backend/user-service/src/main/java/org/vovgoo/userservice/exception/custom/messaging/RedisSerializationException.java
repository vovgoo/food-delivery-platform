package org.vovgoo.userservice.exception.custom.messaging;

public class RedisSerializationException extends RuntimeException {
    public RedisSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
