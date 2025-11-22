package org.vovgoo.orderservice.exception.custom.address;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
        super("Адрес не найден");
    }
}
