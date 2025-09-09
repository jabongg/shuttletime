package com.shuttletime.controller;

import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shuttletime.enums.PaymentStatus;
import com.shuttletime.model.dto.PaymentVerificationRequest;
import com.shuttletime.model.dto.PaymentsVerificationResponse;
import com.shuttletime.model.entity.BadmintonCourt;
import com.shuttletime.model.entity.Booking;
import com.shuttletime.model.entity.Payment;
import com.shuttletime.model.entity.User;
import com.shuttletime.repository.BookingRepository;
import com.shuttletime.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
    private static final Gson gson = new Gson();
    private final RazorpayClient razorpayClient;

    private static final String KEY_ID = "rzp_test_RF9TemS9XUx4ZJ"; // replace with your key id
    private static final String KEY_SECRET = "bFiwGXqGGI5HKoWWxoU3qdX9"; // replace with your key secret

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepo;

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
        orderRequest.put("amount", amount); // Razorpay expects amount in paise
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
    public PaymentsVerificationResponse verifyPayments(@RequestBody PaymentVerificationRequest request) throws Exception {
        // 0. generate signature to match and verify
        String generatedSignature = generateSignature(
                request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
                KEY_SECRET
        );

        // check signature
        if (!generatedSignature.equals(request.getRazorpaySignature())) {
            log.error("Payment verification failed: invalid signature.");
            throw new Exception("Payment verification failed: invalid signature.");
        }

        // 1. Save payment in DB
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
        payment.setAmount(request.getAmount());
        payment.setUserId(request.getUserId());
        payment.setCourtId(request.getCourtId());

        if (generatedSignature.equals(request.getRazorpaySignature())) {
            payment.setRazorpayStatus(PaymentStatus.SUCCESS);
            payment.setStatus(PaymentStatus.SUCCESS.toString());
            //paymentRepository.save(payment);
            log.info("Payment verified successfully!");
        } else {
            payment.setRazorpayStatus(PaymentStatus.FAILED);
            payment.setStatus(PaymentStatus.FAILED.toString());
            //paymentRepository.save(payment);
            log.info("Payment verification failed: invalid signature.");
        }

        Payment savedPayment = paymentRepository.save(payment);
        log.info("saved payment : ", gson.toJson(savedPayment));


        // 2. Fetch managed references
        User user = entityManager.getReference(User.class, request.getUserId());
        BadmintonCourt court = entityManager.getReference(BadmintonCourt.class, Long.valueOf(request.getCourtId()));

        // 3. Save booking linked with payment
        Booking booking = new Booking();
        booking.setUser(user); // ✅ managed entity
        booking.setBadmintonCourt(court); // ✅ managed entity
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPayment(payment);

        log.info("booking info being saved : ", gson.toJson(booking));
        Booking savedBooking = bookingRepo.save(booking);

        // 4. create response
        PaymentsVerificationResponse paymentsVerifyResponse = new PaymentsVerificationResponse();
        paymentsVerifyResponse.setTransactionId(savedPayment.getTransactionId());
        paymentsVerifyResponse.setCourtId(savedPayment.getCourtId());
        paymentsVerifyResponse.setUserId(savedPayment.getUserId());
        paymentsVerifyResponse.setBookingId(savedBooking.getId());
        payment.setAmount(savedPayment.getAmount());
        paymentsVerifyResponse.setMessage("SUCCESS");
        paymentsVerifyResponse.setStatus(savedPayment.getStatus());
        log.info("paymentsVerifyResponse : ", gson.toJson(paymentsVerifyResponse));
        return paymentsVerifyResponse;
    }


    private String generateSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());
        String generatedSignature = Hex.encodeHexString(mac.doFinal(data.getBytes()));
        return generatedSignature;
        //return new String(Base64.getEncoder().encode(hash));
    }
}


