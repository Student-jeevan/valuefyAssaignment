package com.portfolio.rebalancing.repository;

import com.portfolio.rebalancing.entity.RebalanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RebalanceSessionRepository extends JpaRepository<RebalanceSession, Long> {

    List<RebalanceSession> findByClientIdOrderBySessionIdDesc(String clientId);
}

