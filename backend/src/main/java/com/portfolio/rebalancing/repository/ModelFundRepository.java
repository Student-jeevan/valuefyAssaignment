package com.portfolio.rebalancing.repository;

import com.portfolio.rebalancing.entity.ModelFund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelFundRepository extends JpaRepository<ModelFund, String> {
}

