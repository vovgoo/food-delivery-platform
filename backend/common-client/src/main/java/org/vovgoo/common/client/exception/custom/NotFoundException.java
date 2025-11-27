package org.vovgoo.common.client.exception.custom;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

