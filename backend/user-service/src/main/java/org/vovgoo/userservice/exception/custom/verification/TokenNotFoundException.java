package org.vovgoo.userservice.exception.custom.verification;

public class TokenNotFoundException extends RuntimeException {
  public TokenNotFoundException() {
    super("Токен не найден или истёк");
  }
}
