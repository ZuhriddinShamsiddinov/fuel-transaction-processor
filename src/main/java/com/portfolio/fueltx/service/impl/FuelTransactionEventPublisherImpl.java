package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.config.FuelTxProperties;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.service.FuelTransactionEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class FuelTransactionEventPublisherImpl implements FuelTransactionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(FuelTransactionEventPublisherImpl.class);

    private final JmsTemplate jmsTemplate;
    private final FuelTxProperties properties;

    public FuelTransactionEventPublisherImpl(JmsTemplate jmsTemplate, FuelTxProperties properties) {
        this.jmsTemplate = jmsTemplate;
        this.properties = properties;
    }

    @Override
    public void publish(FuelTransactionWebhookRequest request) {
        String queue = properties.getJms().getQueue();
        jmsTemplate.convertAndSend(queue, request);
        log.info("Published fuel transaction event externalId={} queue={}",
                request.getExternalTransactionId(), queue);
    }
}
