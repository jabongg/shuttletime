package com.shuttletime.controller;

import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shuttletime.enums.PaymentStatus;
import com.shuttletime.model.dto.PaymentVerificationRequest;
import com.shuttletime.model.dto.PaymentsVerifyResponse;
import com.shuttletime.model.entity.Payment;
import com.shuttletime.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

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
    private static final Logger log = LoggerFactory.getLogger(RazorpayController.class);

    private final RazorpayClient razorpayClient;

    private static final String KEY_ID = "rzp_test_RF9TemS9XUx4ZJ"; // replace with your key id
    private static final String KEY_SECRET = "bFiwGXqGGI5HKoWWxoU3qdX9"; // replace with your key secret

    @Autowired
    private PaymentRepository paymentRepository;

    public RazorpayController(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Operation(summary = "Create Razorpay Order", description = "Creates a new Razorpay order for the given amount")
    @PostMapping("/create-order")
    public String createOrder(
            @Parameter(description = "Order amount in INR", example = "500")
            @RequestParam("amount") Double amount
    ) throws RazorpayException {
        log.info("creating razorpay order");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Razorpay expects amount in paise
        orderRequest.put("currency", "INR");

        Order order = razorpayClient.orders.create(orderRequest);
        log.info(new Gson().toJson(order));
        return order.toString();
    }

    @Operation(summary = "Get Razorpay Key", description = "Returns the Razorpay Key ID to be used in frontend checkout")
    @GetMapping("/get-key")
    public String getKey() {
        return KEY_ID;
    }

    @PostMapping("/verify")
    public String verifyPayments(@RequestBody PaymentVerificationRequest request) throws Exception {
        String generatedSignature = generateSignature(
                request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
                KEY_SECRET
        );

        // check signature
        if (!generatedSignature.equals(request.getRazorpaySignature())) {
            return "Payment verification failed: invalid signature.";
        }

        // ✅ Save or update in DB
        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId());
        Payment payment;
        if (paymentOpt.isPresent()) {
            payment = paymentOpt.get();
        } else {
            payment = new Payment();
            payment.setRazorpayOrderId(request.getRazorpayOrderId());
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());

        if (generatedSignature.equals(request.getRazorpaySignature())) {
            payment.setStatus(PaymentStatus.SUCCESS.toString());
            paymentRepository.save(payment);
            return "Payment verified successfully!";
        } else {
            payment.setStatus(PaymentStatus.FAILED.toString());
            paymentRepository.save(payment);
            return "Payment verification failed: invalid signature.";
        }
    }

    @PostMapping("/verify-payment")
    public PaymentsVerifyResponse verifyPayment(PaymentVerificationRequest request) throws Exception {
        // 1️⃣ Generate signature
        String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(KEY_SECRET.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        String generatedSignature = Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes()));

        // 2️⃣ Compare with Razorpay signature
        if (!generatedSignature.equals(request.getRazorpaySignature())) {
            throw new RuntimeException("Razorpay signature mismatch! Payment verification failed.");
        }

        // 3️⃣ Save payment in DB
        Payment payment = new Payment();
        payment.setUserId(UUID.fromString(request.getUserId()));
        payment.setCourtId(request.getCourtId());
        payment.setRazorpayOrderId(request.getRazorpayOrderId());
        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setAmount(request.getAmount());

        Payment savedPayment = paymentRepository.save(payment);
        PaymentsVerifyResponse paymentsVerifyResponse = new PaymentsVerifyResponse();
        paymentsVerifyResponse.setBookingId(savedPayment.getBooking().getId());
        paymentsVerifyResponse.setMessage("SUCCESS");
        return paymentsVerifyResponse;
    }


    private String generateSignature (String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());
        String generatedSignature = Hex.encodeHexString(mac.doFinal(data.getBytes()));
        return generatedSignature;
        //return new String(Base64.getEncoder().encode(hash));
    }
}


