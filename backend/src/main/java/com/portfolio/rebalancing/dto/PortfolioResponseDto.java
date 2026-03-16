package com.portfolio.rebalancing.dto;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioResponseDto(
        String clientId,
        BigDecimal portfolioValue,
        List<PortfolioItemDto> items
) {
}

