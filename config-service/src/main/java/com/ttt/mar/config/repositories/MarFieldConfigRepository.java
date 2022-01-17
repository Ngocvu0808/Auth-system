package com.ttt.mar.config.repositories;


import com.ttt.mar.config.entities.MarFieldConfig;
import com.ttt.mar.config.entities.MarFieldConfigType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarFieldConfigRepository extends JpaRepository<MarFieldConfig, Integer>,
    JpaSpecificationExecutor<MarFieldConfig> {

  @Query("SELECT m FROM MarFieldConfig m WHERE m.key = :key AND m.isDeleted = FALSE ")
  Optional<MarFieldConfig> findActiveConfigByKey(@Param("key") String key);

  List<MarFieldConfig> findAllByTypeAndIsDeletedFalse(MarFieldConfigType type);

  List<MarFieldConfig> findAllByTypeAndObjectInAndIsDeletedFalse(MarFieldConfigType type,
      List<String> objects);

  List<MarFieldConfig> findAllByObjectInAndIsDeletedFalse(List<String> objects);
}
