package com.portfolio.rebalancing.repository;

import com.portfolio.rebalancing.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}

