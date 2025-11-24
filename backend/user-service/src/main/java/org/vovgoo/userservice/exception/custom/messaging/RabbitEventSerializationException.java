package org.vovgoo.userservice.exception.custom.messaging;

public class RabbitEventSerializationException extends RuntimeException {
    public RabbitEventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
