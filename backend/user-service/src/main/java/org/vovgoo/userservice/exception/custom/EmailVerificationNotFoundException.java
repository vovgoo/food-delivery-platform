package org.vovgoo.userservice.exception.custom;

public class EmailVerificationNotFoundException extends RuntimeException {
    public EmailVerificationNotFoundException() {
        super("Ссылка для подтверждения не найдена или истекла");
    }
}
