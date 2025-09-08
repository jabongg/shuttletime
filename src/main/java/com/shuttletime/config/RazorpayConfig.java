package com.shuttletime.config;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    /**
     * payload :
     *
     * {amount: 70000, amount_paid: 0, notes: [],
     * created_at: 1757346266,
     * amount_due: 70000, currency: "INR",
     entity
     :
     "order"
     id
     :
     "order_RFA3jOZVVXB2cz"
     notes
     :
     []
     offer_id
     :
     null
     receipt
     :
     null
     status
     :
     "created"
     *
     * Response object :
     * {
     *     "amount": 70000,
     *     "amount_paid": 0,
     *     "notes": [],
     *     "created_at": 1757346266,
     *     "amount_due": 70000,
     *     "currency": "INR",
     *     "receipt": null,
     *     "id": "order_RFA3jOZVVXB2cz",
     *     "entity": "order",
     *     "offer_id": null,
     *     "attempts": 0,
     *     "status": "created"
     * }
     * @return
     * @throws Exception
     */

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        // ⚠️ Replace with your Razorpay credentials
        String keyId = "rzp_test_RF9TemS9XUx4ZJ";
        String keySecret = "bFiwGXqGGI5HKoWWxoU3qdX9";

        return new RazorpayClient(keyId, keySecret);
    }

}
