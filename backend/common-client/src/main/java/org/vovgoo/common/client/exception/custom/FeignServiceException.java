package org.vovgoo.common.client.exception.custom;

public class FeignServiceException extends RuntimeException {
    public FeignServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
