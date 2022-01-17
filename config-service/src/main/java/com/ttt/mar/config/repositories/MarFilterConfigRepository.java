package com.ttt.mar.config.repositories;

import com.ttt.mar.config.entities.MarFilterConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MarFilterConfigRepository extends JpaRepository<MarFilterConfig, Integer>,
    JpaSpecificationExecutor<MarFilterConfig> {

}
