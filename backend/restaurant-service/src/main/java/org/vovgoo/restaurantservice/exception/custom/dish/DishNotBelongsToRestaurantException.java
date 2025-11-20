package org.vovgoo.restaurantservice.exception.custom.dish;

public class DishNotBelongsToRestaurantException extends RuntimeException {
    public DishNotBelongsToRestaurantException(Long dishId, Long restaurantId) {
        super("Блюдо с id " + dishId + " не принадлежит ресторану с id " + restaurantId);
    }
}
