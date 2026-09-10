package com.portfolio.fueltx.exception;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.exception
 * @since 2026-09-10T12:15:00
 */
public class InvalidWebhookSignatureException extends RuntimeException {

    public InvalidWebhookSignatureException(String message) {
        super(message);
    }
}
