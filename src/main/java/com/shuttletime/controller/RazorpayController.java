package com.shuttletime.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Razorpay API", description = "Endpoints for Razorpay payment integration")
@RestController
@RequestMapping("/razorpay")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class RazorpayController {

    private final RazorpayClient razorpayClient;

    private static final String KEY_ID = "rzp_test_RF9TemS9XUx4ZJ"; // replace with your key id
    private static final String KEY_SECRET = "bFiwGXqGGI5HKoWWxoU3qdX9"; // replace with your key secret

    public RazorpayController(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Operation(summary = "Create Razorpay Order", description = "Creates a new Razorpay order for the given amount")
    @PostMapping("/create-order")
    public String createOrder(
            @Parameter(description = "Order amount in INR", example = "500")
            @RequestParam("amount") Double amount
    ) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Razorpay expects amount in paise
        orderRequest.put("currency", "INR");

        Order order = razorpayClient.orders.create(orderRequest);
        return order.toString();
    }

    @Operation(summary = "Get Razorpay Key", description = "Returns the Razorpay Key ID to be used in frontend checkout")
    @GetMapping("/get-key")
    public String getKey() {
        return KEY_ID;
    }
}
