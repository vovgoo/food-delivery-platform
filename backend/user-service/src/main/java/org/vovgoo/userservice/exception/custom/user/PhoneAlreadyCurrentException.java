package org.vovgoo.userservice.exception.custom.user;

public class PhoneAlreadyCurrentException extends RuntimeException {
    public PhoneAlreadyCurrentException() {
        super("Новый телефон совпадает с текущим");
    }
}
