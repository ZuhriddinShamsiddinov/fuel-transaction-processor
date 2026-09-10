package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.exception.BusinessRuleException;
import com.portfolio.fueltx.exception.ResourceNotFoundException;
import com.portfolio.fueltx.mapper.FuelTransactionMapper;
import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.model.dto.TransactionReviewRequest;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import com.portfolio.fueltx.service.DriverSummaryCacheService;
import com.portfolio.fueltx.service.FraudCheckService;
import com.portfolio.fueltx.service.TransactionProcessorService;
import com.portfolio.fueltx.websocket.TransactionEventHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class TransactionProcessorServiceImpl implements TransactionProcessorService {

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessorServiceImpl.class);

    private final FuelTransactionRepository repository;
    private final FraudCheckService fraudCheckService;
    private final DriverSummaryCacheService summaryCacheService;
    private final FuelTransactionMapper mapper;
    private final TransactionEventHub eventHub;

    public TransactionProcessorServiceImpl(
            FuelTransactionRepository repository,
            FraudCheckService fraudCheckService,
            DriverSummaryCacheService summaryCacheService,
            FuelTransactionMapper mapper,
            TransactionEventHub eventHub) {
        this.repository = repository;
        this.fraudCheckService = fraudCheckService;
        this.summaryCacheService = summaryCacheService;
        this.mapper = mapper;
        this.eventHub = eventHub;
    }

    @Override
    public Mono<FuelTransaction> process(FuelTransactionWebhookRequest request) {
        if (request == null || request.getExternalTransactionId() == null) {
            return Mono.empty();
        }

        return repository.existsByExternalTransactionId(request.getExternalTransactionId())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.info("Duplicate ignored externalId={}", request.getExternalTransactionId());
                        return Mono.empty();
                    }
                    return persistNewTransaction(request);
                });
    }

    @Override
    public Mono<FuelTransactionResponse> review(UUID id, TransactionReviewRequest reviewRequest) {
        if (reviewRequest == null || reviewRequest.getDecision() == null) {
            return Mono.error(new IllegalArgumentException("decision is required"));
        }

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Transaction not found: " + id)))
                .flatMap(existing -> {
                    existing.markPersisted();
                    return applyReview(existing, reviewRequest.getDecision());
                })
                .map(mapper::toResponse);
    }

    private Mono<FuelTransaction> persistNewTransaction(FuelTransactionWebhookRequest request) {
        return fraudCheckService.shouldFlag(request)
                .map(flagged -> Boolean.TRUE.equals(flagged)
                        ? TransactionStatus.FLAGGED
                        : TransactionStatus.PROCESSED)
                .map(status -> mapper.toNewEntity(request, status))
                .flatMap(repository::save)
                .map(saved -> {
                    saved.markPersisted();
                    return saved;
                })
                .flatMap(saved -> afterPersist(saved).thenReturn(saved));
    }

    private Mono<Void> afterPersist(FuelTransaction saved) {
        FuelTransactionResponse response = mapper.toResponse(saved);
        eventHub.publish(response);

        if (saved.getStatus() != TransactionStatus.PROCESSED) {
            log.info("Stored flagged transaction id={} externalId={}",
                    saved.getId(), saved.getExternalTransactionId());
            return Mono.empty();
        }

        return summaryCacheService.refreshAllPeriods(saved.getDriverId())
                .doOnSuccess(v -> log.info("Processed transaction id={} externalId={}",
                        saved.getId(), saved.getExternalTransactionId()))
                .onErrorResume(ex -> {
                    log.debug("Cache refresh failed for driver {}: {}",
                            saved.getDriverId(), ex.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<FuelTransaction> applyReview(
            FuelTransaction existing,
            TransactionReviewRequest.ReviewDecision decision) {
        if (existing.getStatus() != TransactionStatus.FLAGGED) {
            return Mono.error(new BusinessRuleException(
                    "Only FLAGGED transactions can be reviewed; current status=" + existing.getStatus()));
        }

        TransactionStatus nextStatus = decision == TransactionReviewRequest.ReviewDecision.APPROVED
                ? TransactionStatus.PROCESSED
                : TransactionStatus.REJECTED;

        existing.setStatus(nextStatus);
        existing.setProcessedAt(Instant.now());

        return repository.save(existing)
                .map(saved -> {
                    saved.markPersisted();
                    return saved;
                })
                .flatMap(saved -> {
                    if (saved.getStatus() != TransactionStatus.PROCESSED) {
                        return Mono.just(saved);
                    }
                    return summaryCacheService.refreshAllPeriods(saved.getDriverId())
                            .thenReturn(saved)
                            .onErrorResume(ex -> {
                                log.debug("Cache refresh after review failed for driver {}: {}",
                                        saved.getDriverId(), ex.getMessage());
                                return Mono.just(saved);
                            });
                });
    }
}
