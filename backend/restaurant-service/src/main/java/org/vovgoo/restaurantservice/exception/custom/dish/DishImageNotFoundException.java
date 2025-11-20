package org.vovgoo.restaurantservice.exception.custom.dish;

public class DishImageNotFoundException extends RuntimeException {
    public DishImageNotFoundException() {
        super("Фотография блюда не найдена");
    }
}
