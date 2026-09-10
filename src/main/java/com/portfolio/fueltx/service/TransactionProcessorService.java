package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.model.dto.TransactionReviewRequest;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface TransactionProcessorService {

    /**
     * Processes a webhook event once: skips duplicates, flags fraud, otherwise marks processed
     * and refreshes the driver spend cache.
     */
    Mono<FuelTransaction> process(FuelTransactionWebhookRequest request);

    /**
     * Approves or rejects a flagged transaction; approved ones enter the spend summary.
     */
    Mono<FuelTransactionResponse> review(UUID id, TransactionReviewRequest reviewRequest);
}
