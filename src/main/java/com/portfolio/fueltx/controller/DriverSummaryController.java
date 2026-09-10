package com.portfolio.fueltx.controller;

import com.portfolio.fueltx.model.dto.DriverFuelSummary;
import com.portfolio.fueltx.model.enums.SummaryPeriod;
import com.portfolio.fueltx.service.DriverSummaryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.controller
 * @since 2026-09-10T12:20:00
 */
@RestController
@RequestMapping("/api/v1/drivers")
public class DriverSummaryController {

    private static final Logger log = LoggerFactory.getLogger(DriverSummaryController.class);

    private final DriverSummaryCacheService summaryCacheService;

    public DriverSummaryController(DriverSummaryCacheService summaryCacheService) {
        this.summaryCacheService = summaryCacheService;
    }

    @GetMapping("/{driverId}/fuel-summary")
    public Mono<DriverFuelSummary> fuelSummary(
            @PathVariable String driverId,
            @RequestParam String period) {
        SummaryPeriod summaryPeriod = SummaryPeriod.fromQueryParam(period);
        log.info("drivers.fuel-summary driverId={} period={}", driverId, summaryPeriod);
        return summaryCacheService.getSummary(driverId, summaryPeriod);
    }
}
