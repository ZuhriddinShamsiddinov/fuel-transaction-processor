package com.portfolio.fueltx.exception;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.exception
 * @since 2026-09-10T12:15:00
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
