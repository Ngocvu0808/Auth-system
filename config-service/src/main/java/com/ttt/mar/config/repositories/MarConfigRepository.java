package com.ttt.mar.config.repositories;

import com.ttt.mar.config.entities.MarConfig;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MarConfigRepository extends JpaRepository<MarConfig, Integer>,
    JpaSpecificationExecutor<MarConfig> {

  @Query("SELECT DISTINCT m.key FROM MarConfig m WHERE m.isDeleted = FALSE ")
  List<String> findDistinctKey();

  List<MarConfig> findAllByKeyAndIsDeletedFalse(String key);
}
