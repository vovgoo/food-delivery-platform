package org.vovgoo.userservice.exception.custom;

public class InvalidOtpException extends RuntimeException {
  public InvalidOtpException() {
    super("Неверный OTP код");
  }
}
