package com.portfolio.rebalancing.controller;

import com.portfolio.rebalancing.dto.RebalanceResponseDto;
import com.portfolio.rebalancing.entity.RebalanceSession;
import com.portfolio.rebalancing.service.RebalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rebalance")
public class RebalanceController {

    private final RebalanceService rebalanceService;

    public RebalanceController(RebalanceService rebalanceService) {
        this.rebalanceService = rebalanceService;
    }

    /**
     * Previews the rebalance recommendations for a client without saving.
     */
    @GetMapping("/{clientId}")
    public RebalanceResponseDto getRebalancePreview(@PathVariable String clientId) {
        return rebalanceService.calculateRebalance(clientId);
    }

    /**
     * Persists the current rebalance recommendations as a session.
     * Expects a JSON body with clientId, e.g., {"clientId": "C001"}
     */
    @PostMapping("/save")
    public RebalanceSession saveRebalance(@RequestBody Map<String, String> request) {
        String clientId = request.get("clientId");
        return rebalanceService.saveRebalance(clientId);
    }

    /**
     * Returns the history of rebalance sessions for a client.
     */
    @GetMapping("/history/{clientId}")
    public List<RebalanceSession> getRebalanceHistory(@PathVariable String clientId) {
        return rebalanceService.getHistory(clientId);
    }
}
