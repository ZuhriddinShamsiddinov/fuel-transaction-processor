package com.portfolio.fueltx.service;

import com.portfolio.fueltx.exception.InvalidWebhookSignatureException;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service
 * @since 2026-09-10T12:55:00
 */
public interface WebhookValidationService {

    /**
     * Verifies {@code X-Webhook-Signature} against the raw request body.
     *
     * @throws InvalidWebhookSignatureException when the signature is missing or invalid
     */
    void validateSignature(String rawBody, String providedSignature);

    /**
     * Computes the HMAC-SHA256 hex digest for the given payload.
     */
    String computeHmacHex(String payload);
}
