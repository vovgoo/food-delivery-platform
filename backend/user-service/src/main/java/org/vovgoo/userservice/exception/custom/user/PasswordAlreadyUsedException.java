package org.vovgoo.userservice.exception.custom.user;

public class PasswordAlreadyUsedException extends RuntimeException {
  public PasswordAlreadyUsedException() {
    super("Новый пароль не должен совпадать со старым");
  }
}
