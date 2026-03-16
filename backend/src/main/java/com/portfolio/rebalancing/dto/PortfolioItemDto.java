package com.portfolio.rebalancing.dto;

import java.math.BigDecimal;

/**
 * One row in a portfolio snapshot.
 * - Includes model funds (even if client has 0 value)
 * - Includes non-model holdings (targetPct = null, isModelFund = false)
 */
public record PortfolioItemDto(
        String fundId,
        String fundName,
        BigDecimal currentValue,
        BigDecimal currentPct,
        BigDecimal targetPct,
        boolean isModelFund
) {
}

