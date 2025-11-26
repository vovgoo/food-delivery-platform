package org.vovgoo.notificationservice.exception;

public class EmailSendException extends RuntimeException {
    public EmailSendException() {
        super("Ошибка отправки сообщения на почту");
    }
}
