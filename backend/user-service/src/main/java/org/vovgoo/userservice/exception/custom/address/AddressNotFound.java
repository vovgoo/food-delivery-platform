package org.vovgoo.userservice.exception.custom.address;

public class AddressNotFound extends RuntimeException {
    public AddressNotFound() {
        super("Адрес не найден");
    }
}
