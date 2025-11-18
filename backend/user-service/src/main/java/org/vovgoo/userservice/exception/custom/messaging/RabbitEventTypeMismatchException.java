package org.vovgoo.userservice.exception.custom.messaging;

public class RabbitEventTypeMismatchException extends RuntimeException {
  public RabbitEventTypeMismatchException(String message) {
    super(message);
  }
}