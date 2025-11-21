package org.vovgoo.orderservice.exception.custom.kafka;

public class InvalidEventTypeException extends RuntimeException {
    public InvalidEventTypeException() {
        super("Некорректный тип события");
    }
}
