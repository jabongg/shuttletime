package com.shuttletime.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void send(String recipient, String subject, String messageBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username); // sender's email
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(messageBody);
        mailSender.send(message);
        log.info("Email sent successfully to {}",recipient);

        // todo : validation
        // todo : exception handling
    }
}
