package org.vovgoo.userservice.exception.custom.verification;

public class TokenAttemptsExceededException extends RuntimeException {
  public TokenAttemptsExceededException() {
    super("Превышено количество попыток ввода токена. Начните процедуру заново.");
  }
}
