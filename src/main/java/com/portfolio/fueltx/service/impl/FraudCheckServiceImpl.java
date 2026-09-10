package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.service.FraudCheckService;
import com.portfolio.fueltx.service.fraud.FraudRule;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class FraudCheckServiceImpl implements FraudCheckService {

    private final List<FraudRule> fraudRules;

    public FraudCheckServiceImpl(List<FraudRule> fraudRules) {
        this.fraudRules = fraudRules;
    }

    @Override
    public Mono<Boolean> shouldFlag(FuelTransactionWebhookRequest request) {
        if (request == null) {
            return Mono.just(false);
        }
        return Flux.fromIterable(fraudRules)
                .concatMap(rule -> rule.matches(request))
                .filter(Boolean::booleanValue)
                .hasElements();
    }
}
