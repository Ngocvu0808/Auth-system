package com.ttt.mar.notify.repositories.template;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ttt.mar.notify.entities.template.TemplateSemanticDetail;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;

public interface TemplateSemanticDetailRepository extends
    JpaRepository<TemplateSemanticDetail, Integer> {

  @Query(value = "select tempDetail from TemplateSemanticDetail tempDetail, TemplateSemantic temp"
      + " where tempDetail.templateSemanticId = temp.id and tempDetail.isDeleted = false "
      + "and temp.isDeleted = false and temp.type =:type and tempDetail.templateId =:templateId")
  List<TemplateSemanticDetail> getTemplateSemanticDetailByTemplateId(
      @Param("type") TemplateSemanticType type, @Param("templateId") Integer templateId);

  @Query(value =
      "Select ts From TemplateSemanticDetail ts Where ts.isDeleted = false And ts.templateId = (:templateId) ")
  List<TemplateSemanticDetail> getTemplateSemanticDetailByTemplateId(
      @Param("templateId") Integer templateId);
}
