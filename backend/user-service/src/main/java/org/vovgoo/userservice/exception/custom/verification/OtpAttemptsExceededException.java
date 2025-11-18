package org.vovgoo.userservice.exception.custom.verification;

public class OtpAttemptsExceededException extends RuntimeException {
  public OtpAttemptsExceededException() {
    super("Превышено количество попыток ввода OTP. Начните процедуру заново.");
  }
}
