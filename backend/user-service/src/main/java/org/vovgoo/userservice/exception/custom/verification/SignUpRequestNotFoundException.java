package org.vovgoo.userservice.exception.custom.verification;

public class SignUpRequestNotFoundException extends RuntimeException {
    public SignUpRequestNotFoundException() {
        super("Запрос на регистрацию не найден или истёк");
    }
}
