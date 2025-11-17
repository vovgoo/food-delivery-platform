package org.vovgoo.userservice.exception.custom.verification;

public class ChangeEmailRequestNotFoundException extends RuntimeException {
    public ChangeEmailRequestNotFoundException() {
        super("Запрос на изменение email не найден или истек");
    }
}
