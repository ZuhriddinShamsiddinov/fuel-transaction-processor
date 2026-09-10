package com.portfolio.fueltx.service;

import com.portfolio.fueltx.model.dto.DriverFuelSummary;
import com.portfolio.fueltx.model.enums.SummaryPeriod;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface DriverSummaryCacheService {

    /**
     * Returns a summary from cache, or aggregates from DB and caches the result.
     */
    Mono<DriverFuelSummary> getSummary(String driverId, SummaryPeriod period);

    /**
     * Recomputes week and month summaries after a processed transaction for the driver.
     */
    Mono<Void> refreshAllPeriods(String driverId);

    /**
     * Aggregates PROCESSED transactions for the period and writes Redis with TTL.
     */
    Mono<DriverFuelSummary> refreshSummary(String driverId, SummaryPeriod period);
}
