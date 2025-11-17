package org.vovgoo.userservice.exception.custom;

public class ChangeEmailRequestNotFoundException extends RuntimeException {
    public ChangeEmailRequestNotFoundException() {
        super("Запрос на изменение email не найден или истек");
    }
}
