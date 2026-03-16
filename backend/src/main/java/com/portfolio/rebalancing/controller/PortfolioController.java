package com.portfolio.rebalancing.controller;

import com.portfolio.rebalancing.dto.PortfolioResponseDto;
import com.portfolio.rebalancing.service.PortfolioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Returns the client's current portfolio snapshot:
     * - total portfolio value
     * - per-fund current value and current allocation %
     * - model target % where applicable
     */
    @GetMapping("/{clientId}")
    public PortfolioResponseDto getPortfolio(@PathVariable String clientId) {
        return portfolioService.getPortfolio(clientId);
    }
}

