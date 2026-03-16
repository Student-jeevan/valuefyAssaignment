package com.portfolio.rebalancing.exception;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        String message,
        int status,
        Instant timestamp,
        List<String> details
) {
}

