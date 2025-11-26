package org.vovgoo.notificationservice.service.sender.sms;

public interface SmsSender {
    void send(String phone, String message);
}
