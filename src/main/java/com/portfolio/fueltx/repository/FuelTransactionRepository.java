package com.portfolio.fueltx.repository;

import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.repository
 * @since 2026-09-10T12:15:00
 */
@Repository
public interface FuelTransactionRepository extends ReactiveCrudRepository<FuelTransaction, UUID> {

    Mono<Boolean> existsByExternalTransactionId(String externalTransactionId);

    Mono<FuelTransaction> findByExternalTransactionId(String externalTransactionId);

    @Query("""
            SELECT * FROM fuel_transactions
            WHERE (:driverId IS NULL OR driver_id = :driverId)
              AND (:fromTs IS NULL OR transaction_timestamp >= :fromTs)
              AND (:toTs IS NULL OR transaction_timestamp <= :toTs)
            ORDER BY transaction_timestamp DESC
            LIMIT :limit OFFSET :offset
            """)
    Flux<FuelTransaction> findFiltered(String driverId, Instant fromTs, Instant toTs, long limit, long offset);

    @Query("""
            SELECT COUNT(*) FROM fuel_transactions
            WHERE (:driverId IS NULL OR driver_id = :driverId)
              AND (:fromTs IS NULL OR transaction_timestamp >= :fromTs)
              AND (:toTs IS NULL OR transaction_timestamp <= :toTs)
            """)
    Mono<Long> countFiltered(String driverId, Instant fromTs, Instant toTs);

    @Query("""
            SELECT COUNT(*) FROM fuel_transactions
            WHERE driver_id = :driverId
              AND transaction_timestamp >= :windowStart
              AND transaction_timestamp <= :windowEnd
              AND status <> 'REJECTED'
            """)
    Mono<Long> countByDriverInWindow(String driverId, Instant windowStart, Instant windowEnd);

    @Query("""
            SELECT * FROM fuel_transactions
            WHERE driver_id = :driverId
              AND transaction_timestamp >= :periodStart
              AND transaction_timestamp <= :periodEnd
              AND status = :status
            """)
    Flux<FuelTransaction> findByDriverAndPeriodAndStatus(
            String driverId,
            Instant periodStart,
            Instant periodEnd,
            TransactionStatus status);

    @Query("""
            SELECT * FROM fuel_transactions
            WHERE (:driverId IS NULL OR driver_id = :driverId)
              AND (:fromTs IS NULL OR transaction_timestamp >= :fromTs)
              AND (:toTs IS NULL OR transaction_timestamp <= :toTs)
            ORDER BY transaction_timestamp ASC
            """)
    Flux<FuelTransaction> findForExport(String driverId, Instant fromTs, Instant toTs);
}
