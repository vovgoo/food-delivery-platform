package org.vovgoo.orderservice.exception.custom.restaurant;

public class RestaurantServiceException extends RuntimeException {
    public RestaurantServiceException() {
        super("Произошла ошибка при обращении к сервису ресторанов");
    }
}
