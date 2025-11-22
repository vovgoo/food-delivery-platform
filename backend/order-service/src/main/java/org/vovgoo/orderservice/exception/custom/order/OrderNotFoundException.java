package org.vovgoo.orderservice.exception.custom.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Заказ не найден");
    }
}
