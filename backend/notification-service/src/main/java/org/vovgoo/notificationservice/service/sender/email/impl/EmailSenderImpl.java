package org.vovgoo.notificationservice.service.sender.email.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.vovgoo.notificationservice.exception.EmailSendException;
import org.vovgoo.notificationservice.service.sender.email.EmailSender;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender javaMailSender;

    @Override
    public void send(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("vovgoo@innowise.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException();
        }
    }
}
