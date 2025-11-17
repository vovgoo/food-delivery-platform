package org.vovgoo.userservice.exception.custom;

import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

public class TokenStrategyNotFoundException extends RuntimeException {
  public TokenStrategyNotFoundException(JwtTokenType type) {
    super("Стратегия токена не найдена для типа: " + type);
  }
}
