package com.portfolio.rebalancing.dto;

import java.math.BigDecimal;

/**
 * DTO for a single rebalance recommendation line item.
 */
public record RebalanceRecommendationDto(
        String fundId,
        String fundName,
        BigDecimal currentPct,
        BigDecimal targetPct,
        BigDecimal drift,
        String action,
        BigDecimal amount,
        BigDecimal postRebalancePct,
        boolean isModelFund
) {
}
