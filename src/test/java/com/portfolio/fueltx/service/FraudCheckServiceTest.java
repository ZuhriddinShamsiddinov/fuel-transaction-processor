package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.service.impl.FraudCheckServiceImpl;
import com.portfolio.fueltx.service.fraud.FraudRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudCheckServiceTest {

    @Mock
    private FraudRule gallonsRule;

    @Mock
    private FraudRule rapidRule;

    private FraudCheckService fraudCheckService;

    @BeforeEach
    void setUp() {
        fraudCheckService = new FraudCheckServiceImpl(List.of(gallonsRule, rapidRule));
    }

    @Test
    void shouldFlag_whenAnyRuleMatches() {
        FuelTransactionWebhookRequest request = sampleRequest();
        when(gallonsRule.matches(any())).thenReturn(Mono.just(false));
        when(rapidRule.matches(any())).thenReturn(Mono.just(true));

        StepVerifier.create(fraudCheckService.shouldFlag(request))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldNotFlag_whenNoRuleMatches() {
        FuelTransactionWebhookRequest request = sampleRequest();
        when(gallonsRule.matches(any())).thenReturn(Mono.just(false));
        when(rapidRule.matches(any())).thenReturn(Mono.just(false));

        StepVerifier.create(fraudCheckService.shouldFlag(request))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void shouldNotFlag_whenRequestNull() {
        StepVerifier.create(fraudCheckService.shouldFlag(null))
                .expectNext(false)
                .verifyComplete();
    }

    private FuelTransactionWebhookRequest sampleRequest() {
        return FuelTransactionWebhookRequest.builder()
                .externalTransactionId("TXN-1")
                .driverId("DRV-1")
                .vehicleId("VEH-1")
                .cardNumberMasked("****1234")
                .merchantName("Pilot")
                .merchantLocation("TX")
                .gallons(new BigDecimal("50"))
                .pricePerGallon(new BigDecimal("3.50"))
                .totalAmount(new BigDecimal("175.00"))
                .transactionTimestamp(Instant.parse("2026-09-10T14:00:00Z"))
                .build();
    }
}
