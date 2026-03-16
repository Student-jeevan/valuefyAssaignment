package com.portfolio.rebalancing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    /**
     * SQLite stores this as REAL.
     * We keep it as Double in the entity for simple JDBC compatibility,
     * and use BigDecimal in service-level financial calculations.
     */
    @Column(name = "total_invested", nullable = false)
    private Double totalInvested;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(Double totalInvested) {
        this.totalInvested = totalInvested;
    }
}

