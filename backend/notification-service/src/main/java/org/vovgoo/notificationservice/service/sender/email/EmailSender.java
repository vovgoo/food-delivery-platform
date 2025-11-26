package org.vovgoo.notificationservice.service.sender.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
