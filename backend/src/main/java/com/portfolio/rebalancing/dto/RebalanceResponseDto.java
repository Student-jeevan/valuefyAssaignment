package com.portfolio.rebalancing.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO containing rebalance summary and detailed recommendations.
 */
public record RebalanceResponseDto(
        String clientId,
        BigDecimal portfolioValue,
        List<RebalanceRecommendationDto> recommendations,
        BigDecimal totalToBuy,
        BigDecimal totalToSell,
        BigDecimal netCashNeeded
) {
}
