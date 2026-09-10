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

    Mono<DriverFuelSummary> getSummary(String driverId, SummaryPeriod period);

    Mono<Void> refreshAllPeriods(String driverId);

    Mono<DriverFuelSummary> refreshSummary(String driverId, SummaryPeriod period);
}
