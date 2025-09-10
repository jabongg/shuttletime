package com.shuttletime.controller;

import com.shuttletime.model.dto.NotificationRequest;
import com.shuttletime.service.EmailService;
import com.shuttletime.service.SMSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Send Email Notification",
            description = "Sends an email to the given recipient with subject and message.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/email")
    public String sendEmail(@RequestBody NotificationRequest emailRequest) {
        emailService.send(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getMessage());
        return "Email sent successfully!";
    }

    @Operation(
            summary = "Send SMS Notification",
            description = "Sends an SMS to the given recipient with message.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "SMS sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/sms")
    public String sendSMS(@RequestBody NotificationRequest smsRequest) {
        smsService.send(smsRequest.getRecipient(), smsRequest.getMessage());
        return "SMS sent successfully!";
    }
}
