package com.shuttletime.controller;

import com.shuttletime.model.dto.NotificationRequest;
import com.shuttletime.service.EmailService;
import com.shuttletime.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SMSService smsService;

    @PostMapping("/email")
    public String sendEmail(@RequestBody NotificationRequest emailRequest) {
        emailService.send(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getMessage());
        return "Email sent successfully!";
    }


    @PostMapping("/sms")
    public String sendSMS(@RequestBody NotificationRequest smsRequest) {
        smsService.send(smsRequest.getRecipient(), smsRequest.getMessage());
        return "SMS sent successfully!";
    }
}
