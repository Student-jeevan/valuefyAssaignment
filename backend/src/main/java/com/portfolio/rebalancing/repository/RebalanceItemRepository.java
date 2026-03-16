package com.portfolio.rebalancing.repository;

import com.portfolio.rebalancing.entity.RebalanceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RebalanceItemRepository extends JpaRepository<RebalanceItem, Long> {

    List<RebalanceItem> findBySessionId(Long sessionId);
}

