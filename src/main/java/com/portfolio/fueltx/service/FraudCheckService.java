package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface FraudCheckService {

    Mono<Boolean> shouldFlag(FuelTransactionWebhookRequest request);
}
