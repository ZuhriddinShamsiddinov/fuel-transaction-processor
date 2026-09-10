package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.PageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface TransactionQueryService {

    Mono<FuelTransactionResponse> findById(UUID id);

    Mono<PageResponse<FuelTransactionResponse>> findPage(
            String driverId,
            Instant from,
            Instant to,
            Integer page,
            Integer size);

    /**
     * Streams CSV rows (header first) for export without loading the full result set.
     */
    Flux<String> exportCsv(String driverId, Instant from, Instant to);
}
