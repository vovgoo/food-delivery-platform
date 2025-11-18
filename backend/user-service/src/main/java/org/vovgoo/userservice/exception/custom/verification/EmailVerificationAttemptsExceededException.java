package org.vovgoo.userservice.exception.custom.verification;

public class EmailVerificationAttemptsExceededException extends RuntimeException {
  public EmailVerificationAttemptsExceededException() {
    super("Превышено количество попыток подтверждения email. Запросите ссылку заново.");
  }
}