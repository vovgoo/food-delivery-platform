package org.vovgoo.restaurantservice.exception.custom.restaurant;

public class RestaurantImageNotFoundException extends RuntimeException {
    public RestaurantImageNotFoundException() {
        super("Фотография ресторана не найдена");
    }
}
