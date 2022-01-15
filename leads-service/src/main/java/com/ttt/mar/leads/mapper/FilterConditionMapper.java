package com.ttt.mar.leads.mapper;

import com.ttt.mar.leads.dto.LeadConditionRequestDto;
import com.ttt.mar.leads.dto.FilterConditionResponseDto;
import com.ttt.mar.leads.entities.FilterCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FilterConditionMapper {

  public abstract FilterConditionResponseDto toDto(FilterCondition entity);

  @Mapping(target = "conditionId", source = "conditionId", ignore = true)
  public abstract FilterCondition fromDto(LeadConditionRequestDto dto);
}
