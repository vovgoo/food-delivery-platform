package org.vovgoo.notificationservice.exception;

public class SmsSendException extends RuntimeException {
    public SmsSendException() {
        super("Ошибка отправки SMS сообщения");
    }
}
