package com.portfolio.rebalancing.service;

import com.portfolio.rebalancing.dto.PortfolioItemDto;
import com.portfolio.rebalancing.dto.PortfolioResponseDto;
import com.portfolio.rebalancing.dto.RebalanceRecommendationDto;
import com.portfolio.rebalancing.dto.RebalanceResponseDto;
import com.portfolio.rebalancing.entity.RebalanceItem;
import com.portfolio.rebalancing.entity.RebalanceSession;
import com.portfolio.rebalancing.repository.RebalanceItemRepository;
import com.portfolio.rebalancing.repository.RebalanceSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RebalanceService {

    private static final int PCT_SCALE = 2;
    private static final int CURRENCY_SCALE = 2;

    private final PortfolioService portfolioService;
    private final RebalanceSessionRepository sessionRepository;
    private final RebalanceItemRepository itemRepository;

    public RebalanceService(
            PortfolioService portfolioService,
            RebalanceSessionRepository sessionRepository,
            RebalanceItemRepository itemRepository
    ) {
        this.portfolioService = portfolioService;
        this.sessionRepository = sessionRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Generates a preview of the rebalance recommendations.
     */
    public RebalanceResponseDto calculateRebalance(String clientId) {
        PortfolioResponseDto portfolio = portfolioService.getPortfolio(clientId);
        BigDecimal portfolioValue = portfolio.portfolioValue();

        List<RebalanceRecommendationDto> recommendations = new ArrayList<>();
        BigDecimal totalToBuy = BigDecimal.ZERO;
        BigDecimal totalToSell = BigDecimal.ZERO;

        for (PortfolioItemDto item : portfolio.items()) {
            BigDecimal targetPct = item.targetPct() != null ? item.targetPct() : BigDecimal.ZERO;
            BigDecimal currentPct = item.currentPct();

            // drift = target_pct - current_pct
            BigDecimal drift = targetPct.subtract(currentPct);

            // amount = drift * portfolioValue / 100
            BigDecimal amount = drift.multiply(portfolioValue)
                    .divide(BigDecimal.valueOf(100), CURRENCY_SCALE, RoundingMode.HALF_UP);

            String action;
            BigDecimal displayAmount;

            if (!item.isModelFund()) {
                action = "REVIEW";
                displayAmount = BigDecimal.ZERO;
            } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
                action = "BUY";
                totalToBuy = totalToBuy.add(amount);
                displayAmount = amount;
            } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
                action = "SELL";
                totalToSell = totalToSell.add(amount.abs());
                displayAmount = amount.abs();
            } else {
                action = "HOLD";
                displayAmount = BigDecimal.ZERO;
            }

            // Post rebalance % should ideally be the target % (if we execute fully)
            BigDecimal postRebalancePct = targetPct;

            recommendations.add(new RebalanceRecommendationDto(
                    item.fundId(),
                    item.fundName(),
                    currentPct.setScale(PCT_SCALE, RoundingMode.HALF_UP),
                    targetPct.setScale(PCT_SCALE, RoundingMode.HALF_UP),
                    drift.setScale(PCT_SCALE, RoundingMode.HALF_UP),
                    action,
                    displayAmount.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP),
                    postRebalancePct.setScale(PCT_SCALE, RoundingMode.HALF_UP),
                    item.isModelFund()
            ));
        }

        BigDecimal netCashNeeded = totalToBuy.subtract(totalToSell);

        return new RebalanceResponseDto(
                clientId,
                portfolioValue,
                recommendations,
                totalToBuy,
                totalToSell,
                netCashNeeded
        );
    }

    /**
     * Persists a rebalance session and its items.
     */
    @Transactional
    public RebalanceSession saveRebalance(String clientId) {
        RebalanceResponseDto rebalance = calculateRebalance(clientId);

        RebalanceSession session = new RebalanceSession();
        session.setClientId(clientId);
        session.setCreatedAt(Instant.now().toString());
        session.setPortfolioValue(rebalance.portfolioValue().doubleValue());
        session.setTotalToBuy(rebalance.totalToBuy().doubleValue());
        session.setTotalToSell(rebalance.totalToSell().doubleValue());
        session.setNetCashNeeded(rebalance.netCashNeeded().doubleValue());
        session.setStatus("APPLIED");

        session = sessionRepository.save(session);

        for (RebalanceRecommendationDto rec : rebalance.recommendations()) {
            if ("HOLD".equals(rec.action())) continue; // Don't clutter DB with HOLD actions

            RebalanceItem item = new RebalanceItem();
            item.setSessionId(session.getSessionId());
            item.setFundId(rec.fundId());
            item.setFundName(rec.fundName());
            item.setAction(rec.action());
            item.setAmount(rec.amount().doubleValue());
            item.setCurrentPct(rec.currentPct().doubleValue());
            item.setTargetPct(rec.targetPct().doubleValue());
            item.setPostRebalancePct(rec.postRebalancePct().doubleValue());
            item.setIsModelFund(rec.isModelFund() ? 1 : 0);

            itemRepository.save(item);
        }

        return session;
    }

    public List<RebalanceSession> getHistory(String clientId) {
        return sessionRepository.findByClientIdOrderByCreatedAtDesc(clientId);
    }
}
