package org.vovgoo.orderservice.exception.custom.kafka;

public class KafkaEventTypeMismatchException extends RuntimeException {
    public KafkaEventTypeMismatchException() {
        super("Некорректный тип события");
    }
}
