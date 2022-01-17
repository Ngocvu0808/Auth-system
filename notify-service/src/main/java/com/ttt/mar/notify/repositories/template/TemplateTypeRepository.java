package com.ttt.mar.notify.repositories.template;

import com.ttt.mar.notify.entities.template.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateTypeRepository extends JpaRepository<TemplateType, Integer> {

  TemplateType findByCode(String code);
}
