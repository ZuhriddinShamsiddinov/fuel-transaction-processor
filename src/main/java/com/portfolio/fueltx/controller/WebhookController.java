package com.portfolio.fueltx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.fueltx.config.WebhookRateLimitFilter;
import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.service.FuelTransactionEventPublisher;
import com.portfolio.fueltx.service.WebhookValidationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.controller
 * @since 2026-09-10T12:20:00
 */
@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final WebhookValidationService validationService;
    private final FuelTransactionEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public WebhookController(
            WebhookValidationService validationService,
            FuelTransactionEventPublisher eventPublisher,
            ObjectMapper objectMapper) {
        this.validationService = validationService;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/fuel-transactions")
    public Mono<ResponseEntity<Void>> ingest(
            @RequestHeader(value = FuelTxConstants.WEBHOOK_SIGNATURE_HEADER, required = false) String signature,
            @Valid @RequestBody FuelTransactionWebhookRequest request,
            ServerWebExchange exchange) {

        String rawBody = exchange.getAttribute(WebhookRateLimitFilter.CACHED_BODY_ATTR);
        if (rawBody == null) {
            rawBody = toJson(request);
        }

        validationService.validateSignature(rawBody, signature);
        eventPublisher.publish(request);

        log.info("webhook.fuel-transactions accepted externalId={}", request.getExternalTransactionId());
        return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build());
    }

    private String toJson(FuelTransactionWebhookRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize webhook payload", ex);
        }
    }
}
