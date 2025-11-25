package org.vovgoo.userservice.exception.custom.address;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(Long limit) {
        super("Нельзя добавить больше " + limit + " адресов");
    }
}
