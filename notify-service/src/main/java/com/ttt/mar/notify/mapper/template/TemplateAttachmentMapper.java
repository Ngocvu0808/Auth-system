package com.ttt.mar.notify.mapper.template;


import com.ttt.mar.notify.dto.template.TemplateAttachmentRequestDto;
import com.ttt.mar.notify.dto.template.TemplateAttachmentResponse;
import com.ttt.mar.notify.entities.template.TemplateAttachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TemplateAttachmentMapper {

  public abstract TemplateAttachment fromDto(TemplateAttachmentRequestDto dto);

  public abstract TemplateAttachmentResponse toDto(TemplateAttachment templateAttachment);
}
