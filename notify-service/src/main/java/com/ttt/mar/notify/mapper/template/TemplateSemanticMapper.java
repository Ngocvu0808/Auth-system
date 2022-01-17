package com.ttt.mar.notify.mapper.template;


import com.ttt.mar.notify.dto.template.TemplateSemanticResponse;
import com.ttt.mar.notify.entities.template.TemplateSemantic;
import org.mapstruct.Mapper;

/**
 * @author NamBN
 * @created_date 06/05/2021
 */

@Mapper(componentModel = "spring")
public abstract class TemplateSemanticMapper {

  public abstract TemplateSemanticResponse toDto(TemplateSemantic entity);


}
