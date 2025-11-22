package org.vovgoo.orderservice.exception.custom.address;

public class AddressDeletedException extends RuntimeException {
    public AddressDeletedException() {
        super("Адрес удалён и недоступен для использования");
    }
}
