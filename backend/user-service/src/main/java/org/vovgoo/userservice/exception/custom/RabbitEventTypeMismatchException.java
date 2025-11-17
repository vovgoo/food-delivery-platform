package org.vovgoo.userservice.exception.custom;

public class RabbitEventTypeMismatchException extends RuntimeException {
  public RabbitEventTypeMismatchException(String message) {
    super(message);
  }
}