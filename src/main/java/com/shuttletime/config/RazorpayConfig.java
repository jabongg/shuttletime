package com.shuttletime.config;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        // ⚠️ Replace with your Razorpay credentials
        String keyId = "rzp_test_RF9TemS9XUx4ZJ";
        String keySecret = "bFiwGXqGGI5HKoWWxoU3qdX9";

        return new RazorpayClient(keyId, keySecret);
    }

}
