package com.portfolio.rebalancing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "model_funds")
public class ModelFund {

    @Id
    @Column(name = "fund_id")
    private String fundId;

    @Column(name = "fund_name", nullable = false)
    private String fundName;

    @Column(name = "asset_class", nullable = false)
    private String assetClass;

    @Column(name = "allocation_pct", nullable = false)
    private Double allocationPct;

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

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public Double getAllocationPct() {
        return allocationPct;
    }

    public void setAllocationPct(Double allocationPct) {
        this.allocationPct = allocationPct;
    }
}

