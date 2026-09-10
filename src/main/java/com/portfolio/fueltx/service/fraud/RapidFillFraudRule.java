package com.portfolio.fueltx.service.fraud;

import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.fraud
 * @since 2026-09-10T12:20:00
 */
@Component
public class RapidFillFraudRule implements FraudRule {

    private final FuelTransactionRepository repository;

    public RapidFillFraudRule(FuelTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Boolean> matches(FuelTransactionWebhookRequest request) {
        if (request == null || request.getDriverId() == null || request.getTransactionTimestamp() == null) {
            return Mono.just(false);
        }

        Instant ts = request.getTransactionTimestamp();
        Instant windowStart = ts.minus(FuelTxConstants.RAPID_FILL_WINDOW);
        Instant windowEnd = ts.plus(FuelTxConstants.RAPID_FILL_WINDOW);

        return repository.countByDriverInWindow(request.getDriverId(), windowStart, windowEnd)
                .map(count -> count + 1 >= FuelTxConstants.RAPID_FILL_THRESHOLD);
    }
}
