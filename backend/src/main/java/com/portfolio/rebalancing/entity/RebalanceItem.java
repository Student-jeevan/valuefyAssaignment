package com.portfolio.rebalancing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rebalance_items")
public class RebalanceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "fund_id", nullable = false)
    private String fundId;

    @Column(name = "fund_name", nullable = false)
    private String fundName;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "current_pct", nullable = false)
    private Double currentPct;

    @Column(name = "target_pct")
    private Double targetPct;

    @Column(name = "post_rebalance_pct")
    private Double postRebalancePct;

    /**
     * SQLite uses INTEGER for booleans typically (0/1).
     */
    @Column(name = "is_model_fund", nullable = false)
    private Integer isModelFund;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCurrentPct() {
        return currentPct;
    }

    public void setCurrentPct(Double currentPct) {
        this.currentPct = currentPct;
    }

    public Double getTargetPct() {
        return targetPct;
    }

    public void setTargetPct(Double targetPct) {
        this.targetPct = targetPct;
    }

    public Double getPostRebalancePct() {
        return postRebalancePct;
    }

    public void setPostRebalancePct(Double postRebalancePct) {
        this.postRebalancePct = postRebalancePct;
    }

    public Integer getIsModelFund() {
        return isModelFund;
    }

    public void setIsModelFund(Integer isModelFund) {
        this.isModelFund = isModelFund;
    }
}

