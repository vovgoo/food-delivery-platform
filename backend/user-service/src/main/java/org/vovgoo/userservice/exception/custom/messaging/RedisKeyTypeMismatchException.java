package org.vovgoo.userservice.exception.custom.messaging;

public class RedisKeyTypeMismatchException extends RuntimeException {
    public RedisKeyTypeMismatchException(String message) {
        super(message);
    }
}