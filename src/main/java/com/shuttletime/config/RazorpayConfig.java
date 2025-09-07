package com.shuttletime.config;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        // ⚠️ Replace with your Razorpay credentials
        String keyId = "rzp_test_xxxxx";
        String keySecret = "your_secret_key";

        return new RazorpayClient(keyId, keySecret);
    }

}
