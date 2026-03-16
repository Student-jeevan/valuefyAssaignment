package com.portfolio.rebalancing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rebalance_sessions")
public class RebalanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    /**
     * SQLite column type is TEXT.
     * We store ISO-8601 timestamps (e.g., 2026-03-16T10:15:30Z) for readability and portability.
     */
    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "portfolio_value", nullable = false)
    private Double portfolioValue;

    @Column(name = "total_to_buy", nullable = false)
    private Double totalToBuy;

    @Column(name = "total_to_sell", nullable = false)
    private Double totalToSell;

    @Column(name = "net_cash_needed", nullable = false)
    private Double netCashNeeded;

    @Column(name = "status", nullable = false)
    private String status;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(Double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public Double getTotalToBuy() {
        return totalToBuy;
    }

    public void setTotalToBuy(Double totalToBuy) {
        this.totalToBuy = totalToBuy;
    }

    public Double getTotalToSell() {
        return totalToSell;
    }

    public void setTotalToSell(Double totalToSell) {
        this.totalToSell = totalToSell;
    }

    public Double getNetCashNeeded() {
        return netCashNeeded;
    }

    public void setNetCashNeeded(Double netCashNeeded) {
        this.netCashNeeded = netCashNeeded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

