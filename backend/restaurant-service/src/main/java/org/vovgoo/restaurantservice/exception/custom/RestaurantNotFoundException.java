package org.vovgoo.restaurantservice.exception.custom;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Ресторан не найден");
    }
}
