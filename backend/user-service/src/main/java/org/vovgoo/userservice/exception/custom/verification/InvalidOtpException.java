package org.vovgoo.userservice.exception.custom.verification;

public class InvalidOtpException extends RuntimeException {
  public InvalidOtpException() {
    super("Неверный OTP код");
  }
}
