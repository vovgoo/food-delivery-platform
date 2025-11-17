package org.vovgoo.userservice.exception.custom;

public class ChangePhoneRequestNotFoundException extends RuntimeException {
    public ChangePhoneRequestNotFoundException() {
        super("Запрос на изменение телефона не найден или истек");
    }
}
