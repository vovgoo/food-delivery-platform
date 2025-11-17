package org.vovgoo.userservice.exception.custom;

public class RabbitEventSerializationException extends RuntimeException {
    public RabbitEventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}