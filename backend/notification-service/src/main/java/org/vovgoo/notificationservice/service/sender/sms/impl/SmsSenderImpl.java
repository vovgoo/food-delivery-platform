package org.vovgoo.notificationservice.service.sender.sms.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.vovgoo.notificationservice.exception.SmsSendException;
import org.vovgoo.notificationservice.service.sender.sms.SmsSender;

/**
 * NOTE: At the moment, no test SMS service was found that would allow sending SMS messages
 * in a test mode without actually sending to real phone numbers.
 *
 * Therefore, for development and testing purposes, email is used as a temporary placeholder
 * via JavaMailSender. This allows testing the notification logic and integration without
 * the risk of sending real SMS messages.
 *
 * The send() method already accepts all the required arguments (phone number and message text),
 * so in the future, it will be easy to replace this implementation with a real SMS service
 * without modifying the rest of the code that uses SmsSender. Integration with a real SMS
 * provider will be limited to this service implementation only.
 */

@Service
@RequiredArgsConstructor
public class SmsSenderImpl implements SmsSender {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender javaMailSender;

    @Override
    public void send(String phone, String message) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("vovgoo@innowise.com");
            helper.setTo(phone);
            helper.setSubject("Sms message");
            helper.setText(String.format("<html>%s</html>", message), true);

            javaMailSender.send(msg);
        } catch (Exception e) {
            throw new SmsSendException();
        }
    }
}
