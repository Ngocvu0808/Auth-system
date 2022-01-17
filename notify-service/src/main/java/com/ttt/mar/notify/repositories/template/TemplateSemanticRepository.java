package com.ttt.mar.notify.repositories.template;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttt.mar.notify.entities.template.TemplateSemantic;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;

@Repository
public interface TemplateSemanticRepository extends JpaRepository<TemplateSemantic, Integer> {

  TemplateSemantic findByIdAndTypeAndIsDeletedFalse(Integer id, TemplateSemanticType type);

  @Query(value = " SELECT t FROM TemplateSemantic t WHERE t.isDeleted= false AND t.type = :type")
  List<TemplateSemantic> getListTemplateSemantic(@Param("type") TemplateSemanticType type);

  @Query(value = "SELECT t FROM TemplateSemantic t WHERE t.isDeleted= false AND t.type = :type AND t.id IN (:listId) ")
  List<TemplateSemantic> findByListIdAndTypeAndIsDeletedFalse(@Param("listId") Set<Integer> listId,
      @Param("type") TemplateSemanticType type);
}
