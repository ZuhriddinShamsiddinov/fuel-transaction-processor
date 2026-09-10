package com.portfolio.fueltx.service.fraud;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

class GallonsThresholdFraudRuleTest {

    private final GallonsThresholdFraudRule rule = new GallonsThresholdFraudRule();

    @Test
    void matches_whenGallonsExceedThreshold() {
        FuelTransactionWebhookRequest request = baseBuilder()
                .gallons(new BigDecimal("200.01"))
                .build();

        StepVerifier.create(rule.matches(request))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void doesNotMatch_whenGallonsWithinLimit() {
        FuelTransactionWebhookRequest request = baseBuilder()
                .gallons(new BigDecimal("200.00"))
                .build();

        StepVerifier.create(rule.matches(request))
                .expectNext(false)
                .verifyComplete();
    }

    private FuelTransactionWebhookRequest.Builder baseBuilder() {
        return FuelTransactionWebhookRequest.builder()
                .externalTransactionId("TXN-1")
                .driverId("DRV-1")
                .vehicleId("VEH-1")
                .cardNumberMasked("****1234")
                .merchantName("Pilot")
                .merchantLocation("TX")
                .pricePerGallon(new BigDecimal("3.50"))
                .totalAmount(new BigDecimal("175.00"))
                .transactionTimestamp(Instant.parse("2026-09-10T14:00:00Z"));
    }
}
