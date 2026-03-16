package com.portfolio.rebalancing.repository;

import com.portfolio.rebalancing.entity.ClientHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientHoldingRepository extends JpaRepository<ClientHolding, Long> {

    List<ClientHolding> findByClientId(String clientId);
}

