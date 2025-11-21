package org.vovgoo.orderservice.exception.custom.address;

public class AddressServiceException extends RuntimeException {
    public AddressServiceException() {
        super("Произошла ошибка при обращении к сервису адресов");
    }
}
