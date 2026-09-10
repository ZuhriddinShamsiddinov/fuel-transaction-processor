package com.portfolio.fueltx.service.fraud;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.fraud
 * @since 2026-09-10T12:20:00
 */
public interface FraudRule {

    /**
     * @return {@code true} when the transaction should be flagged
     */
    Mono<Boolean> matches(FuelTransactionWebhookRequest request);
}
