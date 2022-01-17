package com.ttt.mar.notify.mapper.template;


import com.ttt.mar.notify.dto.template.TemplateVariableResponseForTemplate;
import com.ttt.mar.notify.dto.template.VariableResponse;
import com.ttt.mar.notify.dto.template.VariablesRequestDto;
import com.ttt.mar.notify.entities.template.TemplateVariables;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VariablesMapper {

  public abstract TemplateVariables fromDto(VariablesRequestDto dto);

  public abstract VariableResponse toDto(TemplateVariables entity);

  public abstract TemplateVariableResponseForTemplate toVariableForTemplate(
      TemplateVariables entity);
}
