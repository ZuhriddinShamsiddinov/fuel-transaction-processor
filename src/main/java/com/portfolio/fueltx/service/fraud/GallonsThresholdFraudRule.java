package com.portfolio.fueltx.service.fraud;

import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.fraud
 * @since 2026-09-10T12:20:00
 */
@Component
public class GallonsThresholdFraudRule implements FraudRule {

    @Override
    public Mono<Boolean> matches(FuelTransactionWebhookRequest request) {
        if (request == null || request.getGallons() == null) {
            return Mono.just(false);
        }
        return Mono.just(request.getGallons().compareTo(FuelTxConstants.MAX_GALLONS_PER_FILL) > 0);
    }
}
