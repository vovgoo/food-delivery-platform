package org.vovgoo.restaurantservice.exception.custom.dish;

public class DishNotFoundException extends RuntimeException {
  public DishNotFoundException() {
    super("Блюдо не найдено");
  }
}
