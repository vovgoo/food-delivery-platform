package org.vovgoo.restaurantservice.exception.custom.restaurant;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Ресторан не найден");
    }
}
