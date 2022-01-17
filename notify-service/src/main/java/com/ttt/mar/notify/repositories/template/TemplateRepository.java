package com.ttt.mar.notify.repositories.template;

import com.ttt.mar.notify.entities.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateRepository extends JpaRepository<Template, Integer>,
    JpaSpecificationExecutor<Template> {

  Template findByCodeAndIsDeletedFalse(String code);

  Template findByCodeAndTypeAndIsDeletedFalse(String code, String type);
}
