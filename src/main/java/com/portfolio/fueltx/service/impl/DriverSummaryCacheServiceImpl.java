package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.config.FuelTxProperties;
import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.model.dto.DriverFuelSummary;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.SummaryPeriod;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import com.portfolio.fueltx.service.DriverSummaryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class DriverSummaryCacheServiceImpl implements DriverSummaryCacheService {

    private static final Logger log = LoggerFactory.getLogger(DriverSummaryCacheServiceImpl.class);

    private final ReactiveRedisTemplate<String, DriverFuelSummary> redisTemplate;
    private final FuelTransactionRepository repository;
    private final FuelTxProperties properties;

    public DriverSummaryCacheServiceImpl(
            ReactiveRedisTemplate<String, DriverFuelSummary> redisTemplate,
            FuelTransactionRepository repository,
            FuelTxProperties properties) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.properties = properties;
    }

    @Override
    public Mono<DriverFuelSummary> getSummary(String driverId, SummaryPeriod period) {
        if (driverId == null || driverId.isBlank()) {
            return Mono.error(new IllegalArgumentException("driverId is required"));
        }
        String cacheKey = cacheKey(driverId, period);
        return redisTemplate.opsForValue().get(cacheKey)
                .switchIfEmpty(Mono.defer(() -> refreshSummary(driverId, period)));
    }

    @Override
    public Mono<Void> refreshAllPeriods(String driverId) {
        return refreshSummary(driverId, SummaryPeriod.WEEK)
                .then(refreshSummary(driverId, SummaryPeriod.MONTH))
                .then();
    }

    @Override
    public Mono<DriverFuelSummary> refreshSummary(String driverId, SummaryPeriod period) {
        Instant periodEnd = Instant.now();
        Instant periodStart = period.periodStart(periodEnd);

        return repository.findByDriverAndPeriodAndStatus(
                        driverId, periodStart, periodEnd, TransactionStatus.PROCESSED)
                .collectList()
                .map(transactions -> aggregate(driverId, period, periodStart, periodEnd, transactions))
                .flatMap(summary -> redisTemplate.opsForValue()
                        .set(cacheKey(driverId, period), summary, properties.getCache().getSummaryTtl())
                        .thenReturn(summary))
                .doOnSuccess(s -> log.debug("Refreshed driver summary driverId={} period={} count={}",
                        driverId, period, s.getTransactionCount()));
    }

    private DriverFuelSummary aggregate(
            String driverId,
            SummaryPeriod period,
            Instant periodStart,
            Instant periodEnd,
            java.util.List<FuelTransaction> transactions) {
        BigDecimal totalGallons = BigDecimal.ZERO;
        BigDecimal totalSpend = BigDecimal.ZERO;
        for (FuelTransaction tx : transactions) {
            if (tx.getGallons() != null) {
                totalGallons = totalGallons.add(tx.getGallons());
            }
            if (tx.getTotalAmount() != null) {
                totalSpend = totalSpend.add(tx.getTotalAmount());
            }
        }
        return DriverFuelSummary.builder()
                .driverId(driverId)
                .totalGallons(totalGallons)
                .totalSpend(totalSpend)
                .transactionCount(transactions.size())
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .period(period.name().toLowerCase(Locale.ROOT))
                .build();
    }

    private String cacheKey(String driverId, SummaryPeriod period) {
        return FuelTxConstants.CACHE_KEY_PREFIX + driverId + ":" + period.name().toLowerCase(Locale.ROOT);
    }
}
