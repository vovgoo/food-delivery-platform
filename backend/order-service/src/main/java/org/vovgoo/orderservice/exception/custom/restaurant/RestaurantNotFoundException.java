package org.vovgoo.orderservice.exception.custom.restaurant;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Ресторан не найден");
    }
}
