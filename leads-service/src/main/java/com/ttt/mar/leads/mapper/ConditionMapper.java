package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.LeadConditionResponseDto;
import com.ttt.mar.leads.entities.Condition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ConditionMapper {

  public abstract LeadConditionResponseDto toDto(Condition entity);

}
