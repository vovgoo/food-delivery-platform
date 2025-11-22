package org.vovgoo.orderservice.exception.custom.dish;

public class DishNotAvailableException extends RuntimeException {
    public DishNotAvailableException() {
        super("Выбранное блюдо недоступно");
    }
}
