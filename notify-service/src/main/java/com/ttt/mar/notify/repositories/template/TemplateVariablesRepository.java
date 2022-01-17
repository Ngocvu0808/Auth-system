package com.ttt.mar.notify.repositories.template;

import com.ttt.mar.notify.entities.template.TemplateVariables;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TemplateVariablesRepository extends JpaRepository<TemplateVariables, Integer>,
    JpaSpecificationExecutor<TemplateVariables> {

  List<TemplateVariables> findByCodeAndIsDeletedFalse(String code);

  TemplateVariables findByIdAndIsDeletedFalse(Integer id);

  @Query(value = "SELECT t FROM TemplateVariables t WHERE t.isDeleted = false")
  List<TemplateVariables> findAllAndIsDeletedFalse();

}
