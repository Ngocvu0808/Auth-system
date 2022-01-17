package com.ttt.mar.notify.mapper.template;


import com.ttt.mar.notify.config.Constants;
import com.ttt.mar.notify.dto.template.TemplateRequestDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDetailDto;
import com.ttt.mar.notify.dto.template.TemplateResponseDto;
import com.ttt.mar.notify.entities.template.Template;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class TemplateMapper {

  @Mapping(target = "cc", source = "cc", ignore = true)
  @Mapping(target = "bcc", source = "bcc", ignore = true)
  @Mapping(target = "subject", source = "subject", ignore = true)
  public abstract Template fromDto(TemplateRequestDto dto);

  @BeforeMapping
  public void beforeMapping(@MappingTarget Template template, TemplateRequestDto dto) {
    if (Constants.EMAIL.equals(dto.getType())) {
      template.setCc(dto.getCc());
      template.setBcc(dto.getBcc());
      template.setSubject(dto.getSubject());
    }
  }

  public abstract TemplateResponseDto toDto(Template template);

  public abstract TemplateResponseDetailDto toResponseDetail(Template template);

}
