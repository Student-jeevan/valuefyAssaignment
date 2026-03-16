package com.portfolio.rebalancing.controller;

import com.portfolio.rebalancing.entity.ModelFund;
import com.portfolio.rebalancing.exception.BadRequestException;
import com.portfolio.rebalancing.repository.ModelFundRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/model-portfolio")
public class ModelPortfolioController {

    private final ModelFundRepository modelFundRepository;

    public ModelPortfolioController(ModelFundRepository modelFundRepository) {
        this.modelFundRepository = modelFundRepository;
    }

    @GetMapping
    public List<ModelFund> getModelPortfolio() {
        return modelFundRepository.findAll();
    }

    /**
     * Updates all model fund allocations.
     * Validation rule: All model fund allocations must sum to exactly 100%.
     */
    @PutMapping
    public List<ModelFund> updateModelPortfolio(@RequestBody List<ModelFund> updatedModelFunds) {
        validateAllocations(updatedModelFunds);

        // For simplicity, we assume the list contains existing fundIds.
        // If we want to replace the whole set, we'd delete and saveAll.
        // For this assignment, saving all (which updates existing by ID) is sufficient.
        return modelFundRepository.saveAll(updatedModelFunds);
    }

    private void validateAllocations(List<ModelFund> modelFunds) {
        if (modelFunds == null || modelFunds.isEmpty()) {
            throw new BadRequestException("Model portfolio cannot be empty.");
        }

        double totalPct = 0.0;
        for (ModelFund fund : modelFunds) {
            if (fund.getFundId() == null || fund.getFundId().isEmpty()) {
                throw new BadRequestException("All model funds must have a valid fundId.");
            }
            if (fund.getAllocationPct() == null) {
                throw new BadRequestException("All model funds must have an allocation percentage.");
            }
            totalPct += fund.getAllocationPct();
        }

        // Using a small epsilon for double comparison if needed, but 100.0 is usually exact for these types of sums
        if (Math.abs(totalPct - 100.0) > 0.0001) {
            throw new BadRequestException("Model fund allocations must sum to exactly 100%. Provided: " + totalPct + "%.");
        }
    }
}
