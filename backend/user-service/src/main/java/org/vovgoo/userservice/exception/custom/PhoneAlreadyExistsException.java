package org.vovgoo.userservice.exception.custom;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String phone) {
        super("Пользователь с телефоном '" + phone + "' уже существует");
    }
}