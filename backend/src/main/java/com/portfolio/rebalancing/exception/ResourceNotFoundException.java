package com.portfolio.rebalancing.exception;

/**
 * Used when a requested resource (e.g. client) doesn't exist.
 * We map this to a 404 response in {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

