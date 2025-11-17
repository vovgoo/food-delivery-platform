package org.vovgoo.userservice.exception.custom;

public class OtpAttemptsExceededException extends RuntimeException {
  public OtpAttemptsExceededException() {
    super("Превышено количество попыток ввода OTP. Начните процедуру заново.");
  }
}
