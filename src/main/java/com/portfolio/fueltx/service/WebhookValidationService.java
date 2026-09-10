package com.portfolio.fueltx.service;

import com.portfolio.fueltx.exception.InvalidWebhookSignatureException;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface WebhookValidationService {

    void validateSignature(String rawBody, String providedSignature);

    String computeHmacHex(String payload);
}
