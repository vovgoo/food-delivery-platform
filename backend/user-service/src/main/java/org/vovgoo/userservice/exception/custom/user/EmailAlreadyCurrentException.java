package org.vovgoo.userservice.exception.custom.user;

public class EmailAlreadyCurrentException extends RuntimeException {
    public EmailAlreadyCurrentException() {
        super("Новый email совпадает с текущим");
    }
}
