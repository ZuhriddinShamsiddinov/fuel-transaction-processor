package com.portfolio.fueltx.controller;

import com.portfolio.fueltx.config.TestJmsConfig;
import com.portfolio.fueltx.config.TestR2dbcSchemaConfig;
import com.portfolio.fueltx.config.TestRedisConfig;
import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import com.portfolio.fueltx.service.WebhookValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Import({TestRedisConfig.class, TestR2dbcSchemaConfig.class, TestJmsConfig.class})
class WebhookControllerIntegrationTest {

    private static final String PAYLOAD = """
            {
              "externalTransactionId": "TXN-IT-001",
              "driverId": "DRV-4471",
              "vehicleId": "VEH-1029",
              "cardNumberMasked": "****4417",
              "merchantName": "Pilot Travel Center",
              "merchantLocation": "Amarillo, TX",
              "gallons": 92.5,
              "pricePerGallon": 3.42,
              "totalAmount": 316.35,
              "transactionTimestamp": "2026-09-10T14:32:00Z"
            }
            """;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WebhookValidationService validationService;

    @Autowired
    private FuelTransactionRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll().block();
    }

    @Test
    void validSignature_returnsAccepted() {
        String signature = validationService.computeHmacHex(PAYLOAD);

        webTestClient.post()
                .uri("/api/v1/webhooks/fuel-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(FuelTxConstants.WEBHOOK_SIGNATURE_HEADER, signature)
                .bodyValue(PAYLOAD)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void invalidSignature_returnsUnauthorized() {
        webTestClient.post()
                .uri("/api/v1/webhooks/fuel-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(FuelTxConstants.WEBHOOK_SIGNATURE_HEADER, "deadbeef")
                .bodyValue(PAYLOAD)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void duplicateTransaction_processedOnceOnly() {
        String signature = validationService.computeHmacHex(PAYLOAD);

        webTestClient.post()
                .uri("/api/v1/webhooks/fuel-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(FuelTxConstants.WEBHOOK_SIGNATURE_HEADER, signature)
                .bodyValue(PAYLOAD)
                .exchange()
                .expectStatus().isAccepted();

        webTestClient.post()
                .uri("/api/v1/webhooks/fuel-transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(FuelTxConstants.WEBHOOK_SIGNATURE_HEADER, signature)
                .bodyValue(PAYLOAD)
                .exchange()
                .expectStatus().isAccepted();

        assertThat(repository.count().block()).isEqualTo(1L);
    }
}
