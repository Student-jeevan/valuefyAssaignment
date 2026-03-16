package com.portfolio.rebalancing.service;

import com.portfolio.rebalancing.dto.PortfolioItemDto;
import com.portfolio.rebalancing.dto.PortfolioResponseDto;
import com.portfolio.rebalancing.entity.ClientHolding;
import com.portfolio.rebalancing.entity.ModelFund;
import com.portfolio.rebalancing.exception.ResourceNotFoundException;
import com.portfolio.rebalancing.repository.ClientHoldingRepository;
import com.portfolio.rebalancing.repository.ClientRepository;
import com.portfolio.rebalancing.repository.ModelFundRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    private static final int PCT_SCALE = 4; // keep more precision for downstream drift calculations

    private final ClientRepository clientRepository;
    private final ModelFundRepository modelFundRepository;
    private final ClientHoldingRepository clientHoldingRepository;

    public PortfolioService(
            ClientRepository clientRepository,
            ModelFundRepository modelFundRepository,
            ClientHoldingRepository clientHoldingRepository
    ) {
        this.clientRepository = clientRepository;
        this.modelFundRepository = modelFundRepository;
        this.clientHoldingRepository = clientHoldingRepository;
    }

    /**
     * Creates a snapshot of the client's portfolio:
     * - portfolioValue = sum(current_value)
     * - currentPct per fund = fundValue / portfolioValue * 100
     * - includes model funds with zero current value (so we can recommend BUY later)
     * - includes non-model holdings (flagged as isModelFund=false)
     */
    public PortfolioResponseDto getPortfolio(String clientId) {
        ensureClientExists(clientId);

        List<ClientHolding> holdings = clientHoldingRepository.findByClientId(clientId);
        List<ModelFund> modelFunds = modelFundRepository.findAll();

        BigDecimal portfolioValue = calculatePortfolioValue(holdings);

        Map<String, BigDecimal> holdingValueByFundId = groupHoldingValueByFund(holdings);

        List<PortfolioItemDto> items = new ArrayList<>();

        // 1) Model funds first (even if client has not invested -> currentValue=0)
        for (ModelFund modelFund : modelFunds) {
            BigDecimal fundValue = holdingValueByFundId.getOrDefault(modelFund.getFundId(), BigDecimal.ZERO);
            items.add(new PortfolioItemDto(
                    modelFund.getFundId(),
                    modelFund.getFundName(),
                    fundValue,
                    calculateCurrentPct(fundValue, portfolioValue),
                    toBigDecimal(modelFund.getAllocationPct()),
                    true
            ));
        }

        // 2) Add funds the client holds that are NOT in the model portfolio
        for (Map.Entry<String, BigDecimal> entry : holdingValueByFundId.entrySet()) {
            String fundId = entry.getKey();
            boolean inModel = modelFunds.stream().anyMatch(m -> m.getFundId().equals(fundId));
            if (inModel) {
                continue;
            }

            String fundName = resolveFundNameFromHoldings(holdings, fundId);
            BigDecimal fundValue = entry.getValue();
            items.add(new PortfolioItemDto(
                    fundId,
                    fundName,
                    fundValue,
                    calculateCurrentPct(fundValue, portfolioValue),
                    null,
                    false
            ));
        }

        // Stable ordering for UI: by fundName (null-safe)
        items.sort(Comparator.comparing(i -> i.fundName() == null ? "" : i.fundName()));

        return new PortfolioResponseDto(clientId, portfolioValue, items);
    }

    private void ensureClientExists(String clientId) {
        boolean exists = clientRepository.existsById(clientId);
        if (!exists) {
            throw new ResourceNotFoundException("Client not found: " + clientId);
        }
    }

    BigDecimal calculatePortfolioValue(List<ClientHolding> holdings) {
        BigDecimal total = BigDecimal.ZERO;
        for (ClientHolding holding : holdings) {
            total = total.add(toBigDecimal(holding.getCurrentValue()));
        }
        return total;
    }

    Map<String, BigDecimal> groupHoldingValueByFund(List<ClientHolding> holdings) {
        Map<String, BigDecimal> totals = new HashMap<>();
        for (ClientHolding holding : holdings) {
            BigDecimal current = totals.getOrDefault(holding.getFundId(), BigDecimal.ZERO);
            totals.put(holding.getFundId(), current.add(toBigDecimal(holding.getCurrentValue())));
        }
        return totals;
    }

    BigDecimal calculateCurrentPct(BigDecimal fundValue, BigDecimal portfolioValue) {
        if (portfolioValue.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(PCT_SCALE, RoundingMode.HALF_UP);
        }
        return fundValue
                .multiply(BigDecimal.valueOf(100))
                .divide(portfolioValue, PCT_SCALE, RoundingMode.HALF_UP);
    }

    private String resolveFundNameFromHoldings(List<ClientHolding> holdings, String fundId) {
        for (ClientHolding holding : holdings) {
            if (fundId.equals(holding.getFundId())) {
                return holding.getFundName();
            }
        }
        return null;
    }

    private BigDecimal toBigDecimal(Double value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value);
    }
}

