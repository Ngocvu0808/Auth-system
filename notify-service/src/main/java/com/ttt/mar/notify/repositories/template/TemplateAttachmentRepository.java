package com.ttt.mar.notify.repositories.template;

import com.ttt.mar.notify.entities.template.TemplateAttachment;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateAttachmentRepository extends JpaRepository<TemplateAttachment, Integer> {

  TemplateAttachment findByIdAndTemplateIdAndIsDeletedFalse(Integer id, Integer templateId);

  List<TemplateAttachment> findByIdIsNotInAndTemplateIdAndIsDeletedFalse(Set<Integer> ids,
      Integer templateId);

  List<TemplateAttachment> findByTemplateIdAndIsDeletedFalse(Integer templateId);
}
