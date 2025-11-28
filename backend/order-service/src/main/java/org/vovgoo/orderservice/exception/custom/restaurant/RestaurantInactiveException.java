package org.vovgoo.orderservice.exception.custom.restaurant;

public class RestaurantInactiveException extends RuntimeException {
    public RestaurantInactiveException() {
        super("Выбранный ресторан не доступен");
    }
}
