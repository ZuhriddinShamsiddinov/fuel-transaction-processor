package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface FuelTransactionEventPublisher {

    /**
     * Enqueues the webhook payload for asynchronous processing.
     */
    void publish(FuelTransactionWebhookRequest request);
}
