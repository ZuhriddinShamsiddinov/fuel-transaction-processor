package com.portfolio.fueltx.jms;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.service.TransactionProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.jms
 * @since 2026-09-10T12:20:00
 */
@Component
public class FuelTransactionListener {

    private static final Logger log = LoggerFactory.getLogger(FuelTransactionListener.class);

    private final TransactionProcessorService processorService;

    public FuelTransactionListener(TransactionProcessorService processorService) {
        this.processorService = processorService;
    }

    @JmsListener(destination = "${fueltx.jms.queue}")
    public void onMessage(FuelTransactionWebhookRequest request) {
        if (request == null) {
            log.warn("Received null fuel transaction message; ignoring");
            return;
        }

        try {
            processorService.process(request).block();
        } catch (Exception ex) {
            log.error("Failed to process fuel transaction externalId={}: {}",
                    request.getExternalTransactionId(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
