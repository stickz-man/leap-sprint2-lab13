package com.neueda.leap.merchantportal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class WebhookController {

    @Value("${webhook.secret}")
    private String webhookSecret;

    private static final long TIMESTAMP_TOLERANCE_MS = 300000;
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    // HMAC-SHA256 signature and timestamp verification to prevent unauthorized webhook access
    @PostMapping("/api/webhooks/payment-status")
    public ResponseEntity<Void> handlePaymentStatusWebhook(
            @RequestBody PaymentStatusEvent event,
            @RequestHeader(value = "X-Webhook-Signature") String signature,
            @RequestHeader(value = "X-Webhook-Timestamp") String timestamp) {

        if (!verifyWebhookSignature(event, signature, timestamp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        payoutStatusUpdater.markSettled(event.getPayoutId(), event.getStatus());
        return ResponseEntity.ok().build();
    }

    private boolean verifyWebhookSignature(PaymentStatusEvent event, String signature, String timestamp) {
        try {
            long requestTime = Long.parseLong(timestamp);
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - requestTime) > TIMESTAMP_TOLERANCE_MS) {
                return false;
            }

            String payload = event.getPayoutId() + "." + event.getStatus() + "." + timestamp;
            String expectedSignature = generateHmacSignature(payload);

            return expectedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String generateHmacSignature(String payload) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(
                webhookSecret.getBytes(StandardCharsets.UTF_8),
                0,
                webhookSecret.getBytes(StandardCharsets.UTF_8).length,
                HMAC_ALGORITHM);
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    private PayoutStatusUpdater payoutStatusUpdater;
}
