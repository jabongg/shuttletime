package com.shuttletime.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // sending sms to user phone number
    public void send(String recipient, String messageBody) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                        new PhoneNumber(recipient),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .create();

        log.info("Sms Sent successfully with request id {}", message.getSid());
    }
}
