package org.vovgoo.user.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException() {
        super("Произошла ошибка при обращении к сервису пользователей");
    }
}
